package soya.framework.restruts.action;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ActionContext {

    protected static ActionContext INSTANCE;

    private final ExecutorService executorService;
    private final ServiceLocator serviceLocator;

    protected Properties properties = new Properties();
    protected ActionMappings actionMappings;

    protected ActionContext(ServiceLocator serviceLocator, ActionMappings actionMappings) {
        this.serviceLocator = serviceLocator;
        this.actionMappings = actionMappings;
        this.executorService = createExecutorService();

        INSTANCE = this;
    }

    protected ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(3);
    }

    public Map<String, String> properties() {
        Map<String, String> map = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>();
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            keys.add((String) enumeration.nextElement());
        }
        Collections.sort(keys);

        keys.forEach(e -> {
            map.put(e, properties.getProperty(e));
        });

        return map;

    }

    public String getProperty(String key) {
        if (properties.contains(key)) {
            return properties.getProperty(key);

        } else if (System.getProperty(key) != null) {
            return System.getProperty(key);

        } else {
            return null;
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public <T> T getService(Class<T> type) {
        return serviceLocator.getService(type);
    }

    public <T> T getService(String name, Class<T> type) {
        return serviceLocator.getService(name, type);
    }

    public ActionMappings getActionMappings() {
        return actionMappings;
    }

    public static ActionContext getInstance() {
        return INSTANCE;
    }

    public static ActionContextBuilder builder() {
        if (INSTANCE != null) {
            throw new IllegalStateException("ActionContext is already created.");
        }

        return new ActionContextBuilder();
    }

    public static class ActionContextBuilder {
        private ServiceLocator serviceLocator;
        private Properties properties = new Properties();

        private DefaultActionMappings actionMappings = new DefaultActionMappings();

        public ActionContextBuilder serviceLocator(ServiceLocator serviceLocator) {
            this.serviceLocator = serviceLocator;
            return this;
        }

        public ActionContextBuilder setProperty(String key, String value) {
            this.properties.setProperty(key, value);
            return this;
        }

        public ActionContextBuilder setProperties(Properties properties) {
            this.properties.putAll(properties);
            return this;
        }

        public ActionContextBuilder scan(String... pkg) {
            for (String pk : pkg) {
                Reflections reflections = new Reflections(pk.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(Domain.class);
                set.forEach(c -> {
                    Domain domain = c.getAnnotation(Domain.class);
                    actionMappings.domains.put(domain.name(), c);
                });
            }

            for (String pk : pkg) {
                Reflections reflections = new Reflections(pk.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(OperationMapping.class);
                set.forEach(c -> {
                    OperationMapping operationMapping = c.getAnnotation(OperationMapping.class);
                    actionMappings.actions.put(ActionName.create(operationMapping.domain(), operationMapping.name()), (Class<? extends ActionCallable>) c);
                });
            }
            return this;
        }

        public ActionMappings getActionMappings() {
            return actionMappings;
        }

        public ActionContext create() {
            ActionContext context = new ActionContext(serviceLocator, actionMappings);
            context.properties = properties;
            return context;
        }
    }

    static class DefaultActionMappings implements ActionMappings {

        private Map<String, Class<?>> domains = new HashMap<>();
        private Map<ActionName, Class<? extends ActionCallable>> actions = new HashMap<>();

        @Override
        public String[] domains() {
            List<String> list = new ArrayList<>(domains.keySet());
            Collections.sort(list);
            return list.toArray(new String[list.size()]);
        }

        @Override
        public Class<?> domainType(String domain) {
            return domains.get(domain);
        }

        @Override
        public ActionName[] actions(String domain) {
            List<ActionName> list = new ArrayList<>();
            if (domain == null) {
                list.addAll(actions.keySet());
            } else {
                actions.keySet().forEach(e -> {
                    if (e.getDomain().equals(domain)) {
                        list.add(e);
                    }

                });

            }

            Collections.sort(list);
            return list.toArray(new ActionName[list.size()]);
        }

        @Override
        public Class<? extends ActionCallable> actionType(ActionName actionName) {
            return actions.get(actionName);
        }

        @Override
        public Field[] parameterFields(Class<? extends ActionCallable> actionType) {
            List<Field> fields = new ArrayList<>();
            Set<String> fieldNames = new HashSet<>();
            Class<?> cls = actionType;
            while (!cls.getName().equals("java.lang.Object")) {

                for (Field field : cls.getDeclaredFields()) {
                    if ((field.getAnnotation(ParameterMapping.class) != null
                            || field.getAnnotation(PayloadMapping.class) != null)
                            && !fieldNames.contains(field.getName())) {

                        fields.add(field);
                        fieldNames.add(field.getName());
                    }
                }

                cls = cls.getSuperclass();
            }

            Collections.sort(fields, new ParameterFieldComparator());
            return fields.toArray(new Field[fields.size()]);
        }

    }

}
