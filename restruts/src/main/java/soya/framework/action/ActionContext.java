package soya.framework.action;

import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ActionContext {

    private static ActionContext INSTANCE;

    private final ExecutorService executorService;
    private final ServiceLocator serviceLocator;

    protected Properties properties = new Properties();

    protected ActionContext(ServiceLocator serviceLocator, Set<String> scanPackages) {
        this.serviceLocator = serviceLocator;
        this.executorService = createExecutorService();

        Set<Class<?>> domains = new HashSet<>();
        Set<Class<?>> actions = new HashSet<>();

        if (scanPackages.isEmpty()) {
            Reflections reflections = new Reflections();
            domains.addAll(reflections.getTypesAnnotatedWith(Domain.class));
            actions.addAll(reflections.getTypesAnnotatedWith(ActionDefinition.class));

        } else {
            scanPackages.forEach(pkg -> {
                Reflections reflections = new Reflections(pkg.trim());
                domains.addAll(reflections.getTypesAnnotatedWith(Domain.class));
                actions.addAll(reflections.getTypesAnnotatedWith(ActionDefinition.class));
            });

        }

        domains.forEach(e -> {
            ActionDomain.builder().fromAnnotation(e.getAnnotation(Domain.class)).create();
        });

        actions.forEach(e -> {
            new ActionClass((Class<? extends ActionCallable>) e);
        });

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

    public Object getService(String name) throws ServiceLocateException {
        return serviceLocator.getService(name);
    }

    public <T> T getService(Class<T> type) throws ServiceLocateException {
        return serviceLocator.getService(type);
    }

    public <T> T getService(String name, Class<T> type) throws ServiceLocateException {
        return serviceLocator.getService(name, type);
    }

    public <T> Map<String, T> getServices(Class<T> type) throws ServiceLocateException {
        return serviceLocator.getServices(type);
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
        private Set<String> scanPackages = new HashSet<>();

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
            for (String p : pkg) {
                scanPackages.add(p);
            }
            return this;
        }

        public ActionContext create() {
            ActionContext context = new ActionContext(serviceLocator, scanPackages);
            context.properties = properties;
            return context;
        }
    }

}
