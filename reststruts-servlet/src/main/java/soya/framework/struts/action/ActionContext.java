package soya.framework.struts.action;

public abstract class ActionContext {
    protected static ActionContext INSTANCE;

    protected ActionContext() {
        INSTANCE = this;
    }

    public abstract <T> T getService(Class<T> type);

    public abstract <T> T getService(String name, Class<T> type);

    public static ActionContext getInstance() {
        return INSTANCE;
    }
}
