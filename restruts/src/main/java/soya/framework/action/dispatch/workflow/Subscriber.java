package soya.framework.action.dispatch.workflow;

public interface Subscriber<T> {
    String address();

    void onEvent(Event<T> event);
}
