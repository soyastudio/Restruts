package soya.framework.action.dispatch.orchestration;

import soya.framework.action.Action;

public class OrchestrationAction<O extends Orchestration, T> extends Action<T> {

    protected O orchestrator;

    @Override
    public final T execute() throws Exception {
        return (T) orchestrator.execute(this);
    }
}
