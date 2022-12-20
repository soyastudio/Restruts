package soya.framework.action.servlet;

import soya.framework.action.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

public class ActionMappings {
    public static String ACTION_MAPPINGS_ATTRIBUTE = "soya.framework.action.ActionMappings";

    private long lastUpdateTime;

    private Map<String, DomainMapping> domains = new HashMap<>();
    private Set<ActionMapping> actions = new HashSet<>();

    private Set<ActionRegistry> registries = new HashSet<>();

    public ActionMappings(ActionRegistrationService registrationService) {

        for (ActionDomain domain : ActionClass.registry().domains()) {
            addDomain(domain.getName(), domain.getPath(), domain.getTitle(), domain.getDescription());
        }

        for (ActionName actionName : ActionClass.actions()) {
            if (!containsDomain(actionName.getDomain())) {
                addDomain(actionName.getDomain());
            }

            ActionClass actionClass = ActionClass.get(actionName);
            ActionDefinition definition = actionClass.getActionType().getAnnotation(ActionDefinition.class);
            ActionMapping mapping = add(actionName, definition.method().name(), definition.path(), definition.produces()[0]);

            mapping.addDescriptions(definition.description());
            mapping.addDescriptions("- Action name: " + actionName);
            mapping.addDescriptions("- Action class: " + actionClass.getActionType().getName());

            for (Field field : actionClass.getActionFields()) {
                ActionProperty actionProperty = field.getAnnotation(ActionProperty.class);
                ParameterMapping pm = new ParameterMapping(field.getName(), actionProperty.parameterType());
                pm.addDescriptions(actionProperty.description());

                mapping.getParameters().add(pm);
            }

        }

        touch();
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void register(ActionRegistry registry) {
        registries.add(registry);
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
        if (ActionClass.get(actionName) != null) {
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

        return null;
    }

}
