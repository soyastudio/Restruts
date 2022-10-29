package soya.framework.action.dispatch.orchestration;

import soya.framework.action.dispatch.ActionDispatchSession;

public interface OrchestrationExecutor<T> {

    T execute(Orchestration orchestration, ActionDispatchSession session);
}
