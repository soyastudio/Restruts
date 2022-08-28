package soya.framework.restruts.action;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ActionContext {

    protected static ActionContext INSTANCE;

    private final ExecutorService executorService;

    protected Properties properties = new Properties();
    protected ActionMappings actionMappings;

    protected ActionContext(ActionMappings actionMappings) {
        this.actionMappings = actionMappings;
        this.executorService = createExecutorService();

        INSTANCE = this;
    }

    protected ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(3);
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

    public abstract <T> T getService(Class<T> type);

    public abstract <T> T getService(String name, Class<T> type);

    public ActionMappings getActionMappings() {
        return actionMappings;
    }

    public static ActionContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultActionContextBuilder().create();
        }
        return INSTANCE;
    }

    public static DefaultActionContextBuilder defaultActionContextBuilder() {
        if (INSTANCE != null) {
            throw new IllegalStateException("ActionContext is already created.");
        }

        return new DefaultActionContextBuilder();
    }

    public static class DefaultActionContextBuilder implements ActionMappings {
        private Properties properties = new Properties();

        private Map<String, Class<?>> domains = new HashMap<>();
        private Map<ActionName, Class<? extends Action>> actions = new HashMap<>();

        public DefaultActionContextBuilder setProperty(String key, String value) {
            this.properties.setProperty(key, value);
            return this;
        }

        public DefaultActionContextBuilder setProperties(Properties properties) {
            this.properties.putAll(properties);
            return this;
        }

        public DefaultActionContextBuilder scan(String... pkg) {
            for (String pk : pkg) {
                Reflections reflections = new Reflections(pk.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(Domain.class);
                set.forEach(c -> {
                    Domain domain = c.getAnnotation(Domain.class);
                    domains.put(domain.name(), c);
                });
            }

            for (String pk : pkg) {
                Reflections reflections = new Reflections(pk.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(OperationMapping.class);
                set.forEach(c -> {
                    OperationMapping operationMapping = c.getAnnotation(OperationMapping.class);
                    actions.put(ActionName.create(operationMapping.domain(), operationMapping.name()), (Class<? extends Action>) c);
                });
            }
            return this;
        }

        public ActionContext create() {
            DefaultActionContext context = new DefaultActionContext(this);
            context.properties = properties;

            return context;
        }

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
            actions.keySet().forEach(e -> {
                if (e.getDomain().equals(domain)) {
                    list.add(e);
                }

            });

            Collections.sort(list);
            return list.toArray(new ActionName[list.size()]);
        }

        @Override
        public Class<? extends Action> actionType(ActionName actionName) {
            return actions.get(actionName);
        }

        @Override
        public Field[] parameterFields(Class<? extends Action> actionType) {
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

            Collections.sort(fields, new ActionServlet.ParameterFieldComparator());
            return fields.toArray(new Field[fields.size()]);
        }
    }

    protected static class DefaultActionContext extends ActionContext {

        protected DefaultActionContext(ActionMappings actionMappings) {
            super(actionMappings);
        }

        @Override
        public <T> T getService(Class<T> type) {
            return null;
        }

        @Override
        public <T> T getService(String name, Class<T> type) {
            return null;
        }
    }

    protected static class ParameterFieldComparator implements Comparator<Field> {

        @Override
        public int compare(Field o1, Field o2) {
            if (o1.getAnnotation(PayloadMapping.class) != null) {
                return 1;
            } else if (o2.getAnnotation(PayloadMapping.class) != null) {
                return -1;
            }

            if (o1.getAnnotation(ParameterMapping.class) != null && o2.getAnnotation(ParameterMapping.class) != null) {
                int result = ParameterMapping.ParameterType.index(o1.getAnnotation(ParameterMapping.class).parameterType())
                        - ParameterMapping.ParameterType.index(o2.getAnnotation(ParameterMapping.class).parameterType());
                if (result != 0) {
                    return result;
                }
            }

            return o1.getName().compareTo(o2.getName());
        }
    }
}
