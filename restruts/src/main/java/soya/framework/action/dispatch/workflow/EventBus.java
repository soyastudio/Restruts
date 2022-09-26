package soya.framework.action.dispatch.workflow;

public interface EventBus {
    void register(Subscriber<?> subscriber);

    void post(Event<?> event);
}
