package soya.framework.action.orchestration.eventbus;

import soya.framework.action.dispatch.ActionDispatchSession;

public interface EventDigester {
    ActionDispatchSession digest(Event event);
}
