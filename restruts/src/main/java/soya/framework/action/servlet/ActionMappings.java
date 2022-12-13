package soya.framework.action.servlet;

import soya.framework.action.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

public class ActionMappings {
    public static String ACTION_MAPPINGS_ATTRIBUTE = "SOYA_FRAMEWORK_ACTION_MAPPINGS";

    private Map<String, DomainMapping> domains = new HashMap<>();
    private Set<ActionMapping> actions = new HashSet<>();

    private ActionFactory defaultFactory;
    private Set<ActionFactory> factories = new LinkedHashSet<>();
    private Map<ActionName, ActionFactory> creators = new HashMap<>();

    private long lastUpdateTime;

    public ActionMappings() {
        defaultFactory = new DefaultActionFactory();
        touch();
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void addActionFactory(ActionFactory actionFactory) {
        factories.add(actionFactory);
        touch();
    }

    public List<DomainMapping> domains() {
        List<DomainMapping> list = new ArrayList<>(domains.values());
        Collections.sort(list);
        return list;
    }

    public DomainMapping getDomain(String name) {
        return domains.get(name);
    }

    public void addDomain(String name) {
        String token = name;
        String path = "/" + token;
        String title = token;
        String description = token;
        addDomain(name, path, title, description);

        touch();
    }

    public void addDomain(String name, String path, String title, String description) {
        domains.put(name, new DomainMapping(name, path, title, description));

        touch();
    }

    public boolean containsDomain(String domainName) {
        return domains.containsKey(domainName);
    }

    public ActionMapping add(ActionName actionName, String httpMethod, String path, String produce) {
        ActionMapping mapping = new ActionMapping(actionName, httpMethod, path, produce);
        if (!domains.containsKey(actionName.getDomain())) {
            addDomain(actionName.getDomain());
        }

        DomainMapping domainMapping = domains.get(actionName.getDomain());
        mapping.getPathMapping().add(domainMapping.getPath());

        domainMapping.add(mapping);
        actions.add(mapping);

        touch();

        return mapping;
    }

    public synchronized void touch() {
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public ActionMapping getActionMapping(HttpServletRequest request) {

        Iterator<ActionMapping> iterator = actions.iterator();
        while (iterator.hasNext()) {
            ActionMapping mapping = iterator.next();
            if (mapping.match(request)) {
                return mapping;
            }
        }

        return null;
    }

    public ActionCallable create(HttpServletRequest request) {
        return create(getActionMapping(request), request);
    }

    private ActionCallable create(ActionMapping mapping, HttpServletRequest request) {
        ActionName actionName = mapping.getActionName();

        ActionFactory factory = null;
        if (defaultFactory.contains(actionName)) {
            factory = defaultFactory;

        } else if (creators.containsKey(actionName)) {
            factory = creators.get(actionName);

        } else {
            for (ActionFactory f : factories) {
                if (f.contains(actionName)) {
                    creators.put(actionName, f);
                    factory = f;
                    break;
                }
            }
        }

        if (factory == null) {
            throw new ActionCreationException("Cannot find ActionFactory for action: " + actionName);
        }




        return factory.create(mapping, request);
    }

    static class DefaultActionFactory implements ActionFactory {

        @Override
        public boolean contains(ActionName actionName) {
            return ActionClass.get(actionName) != null;
        }

        @Override
        public ActionCallable create(ActionMapping mapping, HttpServletRequest request) {
            ActionClass actionClass = ActionClass.get(mapping.getActionName());
            ActionCallable action = actionClass.newInstance();

            mapping.getParameters().forEach(pm -> {
                Field field = actionClass.getActionField(pm.getName());
                Object value = ConvertUtils.convert(mapping.getParameterValue(request, pm), field.getType());

                field.setAccessible(true);
                try {
                    field.set(action, value);

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);

                }
            });

            return action;
        }
    }

}
