package soya.framework.action.orchestration.pipeline;

import soya.framework.action.dispatch.ActionDispatchSession;

public interface Task<T> {
    T execute(ActionDispatchSession session);
}
