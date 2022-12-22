package soya.framework.action.dispatch;

import org.apache.commons.beanutils.DynaProperty;
import soya.framework.action.*;

import java.util.*;

public class DynaDispatchActionRegistry implements ActionRegistry, ActionFactory {
    public static final String NAME = "DYNA_DISPATCH_ACTION";

    private final String id;
    private long lastUpdatedTime;

    private Set<ActionDomain> domains = new HashSet<>();
    private Map<ActionName, DynaDispatchActionClass> actionClasses = new HashMap<>();

    public DynaDispatchActionRegistry(String id) {
        this.id = id;
        this.lastUpdatedTime = System.currentTimeMillis();

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
            actionDescriptions.add(describe(e.getValue()));
        });

        return actionDescriptions;
    }

    private ActionDescription describe(DynaDispatchActionClass actionClass) {
        ActionDescription.Builder builder = ActionDescription.builder();
        builder
                .actionName(actionClass.getActionName())
                .actionType(DynaDispatchActionClass.class.getName());

        for(DynaProperty property : actionClass.getDynaProperties()) {
            builder.addProperty(ActionPropertyDescription.create(property.getName(), property.getType(), actionClass.getActionProperty(property.getName())));
        }

        return builder.create();
    }

    @Override
    public ActionFactory actionFactory() {
        return this;
    }

    @Override
    public ActionBean create(ActionName actionName) {
        if(actionClasses.containsKey(actionName)) {
            ActionCallable action =  actionClasses.get(actionName).newInstance();


        }

        throw new IllegalArgumentException("Cannot find action class with name: " + actionName);
    }

    public boolean addDomain(ActionDomain domain) {
        return domains.add(domain);
    }

    public void create(ActionName actionName, String dispatch) {
        if(actionClasses.containsKey(actionName)) {
            throw new IllegalArgumentException("Action name already exists: " + actionName);
        }

        actionClasses.put(actionName, new DynaDispatchActionClass(actionName, dispatch));

    }

    protected void refresh() {

    }
}
