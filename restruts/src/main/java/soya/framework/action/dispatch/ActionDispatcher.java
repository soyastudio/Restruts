package soya.framework.action.dispatch;

import soya.framework.action.ActionCallable;
import soya.framework.action.ActionExecutor;

public class ActionDispatcher {
    private ActionExecutor executor;

    private ActionDispatcher(ActionExecutor executor) {
        this.executor = executor;
    }

    public static ActionDispatcher create(Class<? extends ActionCallable> actionType) {
        return new ActionDispatcher(ActionExecutor.executor(actionType));
    }
}
