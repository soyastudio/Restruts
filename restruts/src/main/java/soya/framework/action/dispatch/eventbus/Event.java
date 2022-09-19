package soya.framework.action.dispatch.eventbus;

public interface Event<T> {
    String address();

    String id();

    long timestamp();

    T getData();
}
