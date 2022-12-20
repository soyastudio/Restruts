package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionContext;
import soya.framework.action.ActionRegistrationService;

public abstract class RegistrationAction<T> extends Action<T> {

    protected ActionRegistrationService registrationService() {
        return ActionContext.getInstance().getActionRegistrationService();
    }
}
