package soya.framework.restruts.action;

import java.util.Properties;

public abstract class ActionContext {

    protected static ActionContext INSTANCE;

    protected Properties properties = new Properties();
    protected ActionMappings actionMappings;

    protected ActionContext(ActionMappings actionMappings) {
        this.actionMappings = actionMappings;
        INSTANCE = this;
    }

    public String getProperty(String key) {
        if (properties.contains(key)) {
            return properties.getProperty(key);

        } else if (System.getProperty(key) != null) {
            return System.getProperty(key);

        } else {
            return null;
        }
    }

    public abstract <T> T getService(Class<T> type);

    public abstract <T> T getService(String name, Class<T> type);

    public ActionMappings getActionMappings() {
        return actionMappings;
    }

    public static ActionContext getInstance() {
        return INSTANCE;
    }

    public class DefaultBuilder {

    }

    class DefaultActionContext extends ActionContext {

        protected DefaultActionContext(ActionMappings actionMappings) {
            super(actionMappings);
        }

        @Override
        public <T> T getService(Class<T> type) {
            return null;
        }

        @Override
        public <T> T getService(String name, Class<T> type) {
            return null;
        }
    }
}
