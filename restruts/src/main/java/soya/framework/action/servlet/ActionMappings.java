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

        for (ActionDomain domain : registrationService.domains()) {
            addDomain(domain.getName(), domain.getPath(), domain.getTitle(), domain.getDescription());
        }

        for (ActionName actionName : registrationService.actions()) {
            if (!containsDomain(actionName.getDomain())) {
                addDomain(actionName.getDomain());
            }

            ActionDescription actionDescription = registrationService.action(actionName);

            ActionMapping mapping = add(actionName, actionDescription.getHttpMethod(), actionDescription.getPath(), actionDescription.getProduces()[0]);

            mapping.addDescriptions(actionDescription.getDescription());
            mapping.addDescriptions("- Action name: " + actionName);
            mapping.addDescriptions("- Action type: " + actionDescription.getActionType());
            mapping.addDescriptions("- Action details: " + actionDescription.getImplementation());

            for (String propName : actionDescription.getActionPropertyNames()) {
                ActionPropertyDescription prop = actionDescription.getActionPropertyDescription(propName);
                ParameterMapping pm = new ParameterMapping(prop.getName(), prop.getParameterType());
                pm.addDescriptions(prop.getDescription());

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
        ActionBean bean = ActionRegistrationService.getInstance().create(actionName);
        mapping.getParameters().forEach(pm -> {
            Class<?> propType = bean.getPropertyType(pm.getName());
            Object value = ConvertUtils.convert(mapping.getParameterValue(request, pm), propType);
            if(value != null) {
               bean.set(pm.getName(), value);
            }
        });

        return bean.getAction();
    }

}
