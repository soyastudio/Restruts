package soya.framework.action;

import java.util.Map;

public interface ServiceLocator {
    String[] serviceNames();

    Object getService(String name);

    <T> T getService(Class<T> type);

    <T> T getService(String name, Class<T> type);

    <T> Map<String, T> getServices(Class<T> type);
}
