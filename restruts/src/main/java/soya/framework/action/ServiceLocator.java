package soya.framework.action;

public interface ServiceLocator {
    Object getService(String name);

    <T> T getService(Class<T> type);

    <T> T getService(String name, Class<T> type);
}
