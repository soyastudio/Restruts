package soya.framework.action.servlet;

import soya.framework.action.ActionCallable;
import soya.framework.action.ActionClass;
import soya.framework.action.ActionName;
import soya.framework.action.Domain;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ActionMappings {
    public static String ACTION_MAPPINGS_ATTRIBUTE = "SOYA_FRAMEWORK_ACTION_MAPPINGS";

    private Map<String, DomainMapping> domains = new HashMap<>();
    private Set<ActionMapping> entrySet = new HashSet<>();

    private ActionFactory actionFactory = new DefaultActionFactory();

    public ActionMappings() {

    }

    public void setActionFactory(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
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
    }

    public void addDomain(String name, String path, String title, String description) {
        domains.put(name, new DomainMapping(name, path, title, description));
    }

    public boolean containsDomain(String domainName) {
        return domains.containsKey(domainName);
    }

    public ActionMapping add(ActionName actionName, String httpMethod, String path, String produce) {
        ActionMapping mapping = new ActionMapping(actionName, httpMethod, path, produce);
        if(!domains.containsKey(actionName.getDomain())) {
            addDomain(actionName.getDomain());
        }

        DomainMapping domainMapping = domains.get(actionName.getDomain());
        mapping.getPathMapping().add(domainMapping.getPath());

        domainMapping.add(mapping);
        entrySet.add(mapping);

        return mapping;
    }

    public ActionMapping get(HttpServletRequest request) {

        Iterator<ActionMapping> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            ActionMapping mapping = iterator.next();
            if (mapping.match(request)) {
                return mapping;
            }
        }

        return null;
    }

    private ActionCallable create(ActionMapping mapping, HttpServletRequest request) {
        ActionCallable action = null;
        ActionName actionName = mapping.getActionName();
        if (ActionClass.get(actionName) != null) {
            ActionClass actionClass = ActionClass.get(actionName);
            action = actionClass.newInstance();

        }

        return action;
    }

    static class DefaultActionFactory implements ActionFactory {

        @Override
        public ActionCallable create(ActionMapping mapping, HttpServletRequest request) {
            return null;
        }
    }

}
