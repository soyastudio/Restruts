package soya.framework.action.dispatch;

import soya.framework.action.*;

import java.util.*;

public class DynaActionDispatchActionRegistry implements ActionRegistry, ActionFactory {
    public static final String NAME = "DYNA_DISPATCH_ACTION";

    private final String id;
    private long lastUpdatedTime;

    private Set<ActionDomain> domains = new HashSet<>();
    private Map<ActionName, DynaActionDispatchActionClass> actionClasses = new HashMap<>();

    public DynaActionDispatchActionRegistry(String id) {
        this.id = id;
        touch();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public long lastUpdatedTime() {
        return lastUpdatedTime;
    }

    @Override
    public Collection<ActionDomain> domains() {
        return domains;
    }

    @Override
    public Collection<ActionDescription> actions() {
        Collection<ActionDescription> actionDescriptions = new ArrayList<>();
        actionClasses.entrySet().forEach(e -> {
            actionDescriptions.add(e.getValue().getActionDescription());
        });

        return actionDescriptions;
    }

    @Override
    public ActionFactory actionFactory() {
        return this;
    }

    @Override
    public ActionBean create(ActionName actionName) {
        if (actionClasses.containsKey(actionName)) {
            ActionCallable action = actionClasses.get(actionName).newInstance();
        }

        throw new IllegalArgumentException("Cannot find action class with name: " + actionName);
    }

    public boolean addDomain(ActionDomain domain) {
        boolean boo = domains.add(domain);
        if (boo) {
            touch();
        }
        return boo;
    }

    public void create(ActionName actionName, String dispatch) {
        if (actionClasses.containsKey(actionName)) {
            throw new IllegalArgumentException("Action name already exists: " + actionName);
        }

        actionClasses.put(actionName, new DynaActionDispatchActionClass(actionName, dispatch));
        touch();
    }

    protected void touch() {
        this.lastUpdatedTime = System.currentTimeMillis();
    }
}
