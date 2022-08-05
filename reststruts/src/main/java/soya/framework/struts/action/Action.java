package soya.framework.struts.action;

public interface Action<T> {
    T execute() throws Exception;
}
