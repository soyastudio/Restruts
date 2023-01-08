package soya.framework.action.orchestration;

public interface Task<T> {
    T execute(ProcessSession session) throws ProcessException;
}
