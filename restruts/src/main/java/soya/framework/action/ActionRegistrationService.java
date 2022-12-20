package soya.framework.action;

import org.reflections.Reflections;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ActionRegistrationService {

    private Registration defaultRegistration;
    private Map<String, Registration> registrations = new ConcurrentHashMap<>();

    ActionRegistrationService(Set<String> scanPackages) {

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
            ActionClass.createActionDomain(e);

        });

        actions.forEach(e -> {
            new ActionClass((Class<? extends ActionCallable>) e);
        });

        this.defaultRegistration = new Registration(ActionClass.registry());
    }

    public synchronized long lastUpdatedTime() {
        AtomicLong timestamp = new AtomicLong(defaultRegistration.registry.lastUpdatedTime());
        registrations.values().forEach(e -> {
            timestamp.set(Math.max(timestamp.get(), e.registry.lastUpdatedTime()));
        });

        return timestamp.get();
    }

    public synchronized void register(ActionRegistry registry) {
        Registration registration = new Registration(registry);
        registrations.put(registry.id(), registration);
    }

    public synchronized void unregister(String registryId) {
        if (registrations.containsKey(registryId)) {
            registrations.remove(registryId);
        }
    }

    public synchronized String[] registers() {
        String[] arr = registrations.keySet().toArray(new String[registrations.size()]);
        Arrays.sort(arr);
        return arr;
    }

    public synchronized ActionDomain[] domains() {
        return defaultRegistration.domains();
    }

    public synchronized ActionDomain[] domains(String registryId) {
        if(registrations.containsKey(registryId)) {
            return registrations.get(registryId).domains();
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionDomain domain(String name) {
        return defaultRegistration.domain(name);
    }

    public synchronized ActionDomain domain(String registryId, String name) {
        if(registrations.containsKey(registryId)) {
            return registrations.get(registryId).domain(name);
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionName[] actions() {
        return defaultRegistration.actions();
    }

    public synchronized ActionName[] actions(String registryId) {
        if(registrations.containsKey(registryId)) {
            return registrations.get(registryId).actions();
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionDescription action(ActionName actionName) {
        return defaultRegistration.actionDescription(actionName);
    }

    public synchronized ActionDescription action(String registryId, ActionName actionName) {
        if(registrations.containsKey(registryId)) {
            return registrations.get(registryId).actionDescription(actionName);
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionCallable create(URI uri) {
        ActionName actionName = ActionName.fromURI(uri);
        String userInfo = uri.getUserInfo();
        if(userInfo == null) {
            return defaultRegistration.registry.actionFactory().create(actionName);

        } else if(registrations.containsKey(userInfo)){
            return registrations.get(userInfo).registry.actionFactory().create(actionName);

        } else {
            throw new ActionCreationException("");
        }
    }

    private static class Registration {
        private final ActionRegistry registry;

        private Map<String, ActionDomain> domains = new LinkedHashMap<>();
        private Map<ActionName, ActionDescription> actions = new LinkedHashMap<>();

        private Registration(ActionRegistry registry) {
            this.registry = registry;
            refresh(registry);

        }

        public ActionDomain[] domains() {
            return domains.values().toArray(new ActionDomain[domains.size()]);
        }

        public ActionDomain domain(String name) {
            return domains.get(name);
        }

        public ActionName[] actions() {
            return actions.keySet().toArray(new ActionName[actions.size()]);
        }

        public ActionDescription actionDescription(ActionName actionName) {
            return actions.get(actionName);
        }

        public void refresh(ActionRegistry registry) {
            this.domains.clear();
            List<ActionDomain> domainList = new ArrayList<>(registry.domains());
            Collections.sort(domainList);
            domainList.forEach(e -> {
                domains.put(e.getName(), e);
            });

            this.actions.clear();
            List<ActionDescription> actionList = new ArrayList<>(registry.actions());
            Collections.sort(actionList);
            actionList.forEach(e -> {
                actions.put(e.getActionName(), e);
            });
        }
    }
}
