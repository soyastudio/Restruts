package soya.framework.action.dispatch.eventbus;

import soya.framework.action.dispatch.ActionDispatchSession;

public interface EventDigester {
    ActionDispatchSession digest(Event event);
}
