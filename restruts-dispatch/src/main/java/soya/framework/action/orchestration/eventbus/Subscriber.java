package soya.framework.action.orchestration.eventbus;

public interface Subscriber {
    void onEvent(Event event);
}
