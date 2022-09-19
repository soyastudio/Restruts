package soya.framework.action.dispatch.eventbus;

public interface Subscriber<T> {
    String address();

    void onEvent(Event<T> event);
}
