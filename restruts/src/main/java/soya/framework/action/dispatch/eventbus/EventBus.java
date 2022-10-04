package soya.framework.action.dispatch.eventbus;

public interface EventBus {
    void register(String address, Subscriber subscriber);

    void dispatch(Event event);
}
