package soya.framework.action.dispatch.eventbus;

public interface Subscriber {
    void onEvent(Event event);
}
