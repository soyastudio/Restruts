package soya.framework.restruts.action;

public interface Action<T> {
    T execute() throws Exception;
}
