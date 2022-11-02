package soya.framework.action.orchestration.eventbus;

public interface EventBus {
    void register(String address, Subscriber subscriber);

    void dispatch(Event event);
}
