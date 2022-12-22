package soya.framework.action.dispatch;

import soya.framework.action.Action;
import soya.framework.action.WiredService;

public abstract class DynaActionRegistryAction<T> extends Action<T> {

    @WiredService
    protected DynaDispatchActionRegistry registry;
}
