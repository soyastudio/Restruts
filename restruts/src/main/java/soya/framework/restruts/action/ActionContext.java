package soya.framework.restruts.action;

public abstract class ActionContext {

    protected static ActionContext INSTANCE;

    protected ActionMappings actionMappings;

    protected ActionContext(ActionMappings actionMappings) {
        this.actionMappings = actionMappings;
        INSTANCE = this;
    }

    public abstract <T> T getService(Class<T> type);

    public abstract <T> T getService(String name, Class<T> type);

    public ActionMappings getActionMappings() {
        return actionMappings;
    }

    public static ActionContext getInstance() {
        return INSTANCE;
    }
}
