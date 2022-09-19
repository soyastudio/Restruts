package soya.framework.action.dispatch.eventbus;

public interface EventBus {
    void register(Subscriber<?> subscriber);

    void post(Event<?> event);
}
