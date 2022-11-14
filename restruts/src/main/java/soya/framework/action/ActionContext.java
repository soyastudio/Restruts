package soya.framework.action;

import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ActionContext {

    private static ActionContext INSTANCE;

    private final ExecutorService executorService;
    private final ServiceLocator serviceLocator;

    protected Properties properties = new Properties();
    protected ActionMappings actionMappings;

    protected ActionContext(ServiceLocator serviceLocator, ActionMappings actionMappings) {
        // Objects.requireNonNull(serviceLocator, "ServiceLocator is required.");
        Objects.requireNonNull("ActionMappings is required.");

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

    public String[] serviceNames() {
        return serviceLocator.serviceNames();
    }

    public Object getService(String name) throws ServiceNotAvailableException {
        return serviceLocator.getService(name);
    }

    public <T> T getService(Class<T> type) throws ServiceNotAvailableException {
        return serviceLocator.getService(type);
    }

    public <T> T getService(String name, Class<T> type) throws ServiceNotAvailableException {
        return serviceLocator.getService(name, type);
    }

    public <T> Map<String, T> getServices(Class<T> type) throws ServiceNotAvailableException {
        return serviceLocator.getServices(type);
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
                List<Class<?>> list = new ArrayList<>(set);
                Collections.sort(list, new DomainClassComparator());

                list.forEach(c -> {
                    Domain domain = c.getAnnotation(Domain.class);
                    actionMappings.domains.put(domain.name(), c);
                });
            }

            for (String pk : pkg) {
                Reflections reflections = new Reflections(pk.trim());
                Set<Class<?>> set = reflections.getTypesAnnotatedWith(ActionDefinition.class);
                set.forEach(c -> {
                    ActionClass actionClass = new ActionClass((Class<? extends ActionCallable>) c);

                    actionMappings.actionClasses.put(actionClass.getActionName(), actionClass);
                    actionMappings.actionTypes.put(actionClass.getActionType(), actionClass);
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

    static class DomainClassComparator implements Comparator<Class<?>> {

        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            return o1.getAnnotation(Domain.class).path().compareTo(o2.getAnnotation(Domain.class).path());
        }
    }

    static class DefaultActionMappings implements ActionMappings {

        private Map<String, Class<?>> domains = new LinkedHashMap<>();
        private Map<ActionName, ActionClass> actionClasses = new HashMap<>();
        private Map<Class<? extends ActionCallable>, ActionClass> actionTypes = new HashMap<>();

        @Override
        public String[] domains() {
            return domains.keySet().toArray(new String[domains.size()]);
        }

        @Override
        public Class<?> domainType(String domain) {
            return domains.get(domain);
        }

        @Override
        public ActionName[] actions(String domain) {
            List<ActionName> list = new ArrayList<>();
            if (domain == null) {
                list.addAll(actionClasses.keySet());
            } else {
                actionClasses.keySet().forEach(e -> {
                    if (e.getDomain().equals(domain)) {
                        list.add(e);
                    }
                });
            }

            Collections.sort(list);
            return list.toArray(new ActionName[list.size()]);
        }

        @Override
        public ActionClass actionClass(ActionName actionName) {
            return actionClasses.get(actionName);
        }

        @Override
        public ActionClass actionClass(Class<? extends ActionCallable> actionType) {
            if (actionType.isInterface() || Modifier.isAbstract(actionType.getModifiers())) {
                throw new IllegalArgumentException("Action type cannot be interface or abstract class");
            }

            if (!actionTypes.containsKey(actionType)) {
                ActionClass actionClass = new ActionClass(actionType);
                actionTypes.put(actionClass.getActionType(), actionClass);
                actionClasses.put(actionClass.getActionName(), actionClass);

            }

            return actionTypes.get(actionType);
        }

    }

}
