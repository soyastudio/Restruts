package soya.framework.action.dispatch;

import soya.framework.action.ActionName;

public interface EventBus {
    void register(ActionName address, Subscriber subscriber);

    void dispatch(Event event);
}
