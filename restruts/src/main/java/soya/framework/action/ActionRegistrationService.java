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

    public static ActionRegistrationService getInstance() {
        return ActionContext.getInstance().getActionRegistrationService();
    }

    public synchronized long lastUpdatedTime() {
        AtomicLong timestamp = new AtomicLong(defaultRegistration.registry.lastUpdatedTime());
        registrations.values().forEach(e -> {
            timestamp.set(Math.max(timestamp.get(), e.registry.lastUpdatedTime()));
        });

        return timestamp.get();
    }

    public synchronized boolean containsDomain(String name) {
        if(defaultRegistration.containsDomain(name)) {
            return true;
        }

        for(Registration registration: registrations.values()) {
            if(registration.containsDomain(name)) {
                return true;
            }
        }

        return false;
    }

    public synchronized boolean containsAction(ActionName actionName) {
        if(defaultRegistration.containsAction(actionName)) {
            return true;
        }

        for(Registration registration: registrations.values()) {
            if(registration.containsAction(actionName)) {
                return true;
            }
        }

        return false;
    }

    public synchronized ActionBean create(ActionName actionName) {
        if(defaultRegistration.containsAction(actionName)) {
            return defaultRegistration.registry.actionFactory().create(actionName);
        }

        for(Registration registration : registrations.values()) {
            if(registration.containsAction(actionName)) {
                return registration.registry.actionFactory().create(actionName);
            }
        }

        throw new IllegalArgumentException("Cannot find action with name: " + actionName);
    }

    public synchronized URI find(ActionName actionName) {
        if (defaultRegistration.actionDescription(actionName) != null) {
            return URI.create(actionName.toString());
        }

        for (Map.Entry<String, Registration> entry : registrations.entrySet()) {
            if (entry.getValue().actionDescription(actionName) != null) {
                return URI.create(
                        new StringBuilder(actionName.getDomain())
                                .append("://")
                                .append(entry.getKey())
                                .append("@")
                                .append(actionName.getName())
                                .toString());
            }
        }

        return null;
    }

    // ------------------- Default Registry:
    public synchronized ActionDomain[] domains() {
        return defaultRegistration.domains();
    }

    public synchronized ActionDomain domain(String name) {
        return defaultRegistration.domain(name);
    }

    public synchronized ActionName[] actions() {
        return defaultRegistration.actions();
    }

    public synchronized ActionDescription action(ActionName actionName) {
        return defaultRegistration.actionDescription(actionName);
    }

    // ------------------- Registrations:
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

    public synchronized ActionDomain[] domains(String registryId) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).domains();
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionDomain domain(String registryId, String name) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).domain(name);
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionName[] actions(String registryId) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).actions();
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    public synchronized ActionDescription action(String registryId, ActionName actionName) {
        if (registrations.containsKey(registryId)) {
            return registrations.get(registryId).actionDescription(actionName);
        }

        throw new IllegalArgumentException("Registry is not defined: " + registryId);
    }

    private static class Registration {
        private final ActionRegistry registry;

        private Map<String, ActionDomain> domains = new LinkedHashMap<>();
        private Map<ActionName, ActionDescription> actions = new LinkedHashMap<>();

        private Registration(ActionRegistry registry) {
            this.registry = registry;
            refresh(registry);

        }

        public boolean containsDomain(String name) {
            return domains.containsKey(name);
        }

        public ActionDomain[] domains() {
            return domains.values().toArray(new ActionDomain[domains.size()]);
        }

        public ActionDomain domain(String name) {
            return domains.get(name);
        }

        public boolean containsAction(ActionName actionName) {
            return actions.containsKey(actionName);
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
