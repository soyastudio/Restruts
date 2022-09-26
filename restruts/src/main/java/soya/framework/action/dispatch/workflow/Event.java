package soya.framework.action.dispatch.workflow;

public interface Event<T> {
    String address();

    String id();

    long timestamp();

    T getData();
}
