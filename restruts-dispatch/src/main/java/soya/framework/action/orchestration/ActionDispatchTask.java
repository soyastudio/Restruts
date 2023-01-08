package soya.framework.action.orchestration;

import soya.framework.action.dispatch.ActionDispatch;

public final class ActionDispatchTask<T> implements Task<T> {
    private ActionDispatch actionDispatch;

    public ActionDispatchTask(ActionDispatch actionDispatch) {
        this.actionDispatch = actionDispatch;
    }

    @Override
    public T execute(ProcessSession session) {
        return (T) actionDispatch.dispatch(session).get();
    }
}
