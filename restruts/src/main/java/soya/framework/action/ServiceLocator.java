package soya.framework.action;

import java.util.Map;

public interface ServiceLocator {
    String[] serviceNames();

    Object getService(String name) throws ServiceNotAvailableException;

    <T> T getService(Class<T> type) throws ServiceNotAvailableException;

    <T> T getService(String name, Class<T> type) throws ServiceNotAvailableException;

    <T> Map<String, T> getServices(Class<T> type) throws ServiceNotAvailableException;
}
