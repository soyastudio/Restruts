package soya.framework.action.dispatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActionProxyFactory {

    private Map<String, Object> proxies = new ConcurrentHashMap<>();

    public ActionProxyFactory() {
    }

    public String[] proxyInterfaces() {
        List<String> list = new ArrayList<>(proxies.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public <T> T create(Class<T> proxyInterface) {
        if(proxyInterface.getAnnotation(ActionProxy.class) == null) {
            throw new IllegalArgumentException("Not annotated as 'ActionProxy' for class: " + proxyInterface.getName());
        }

        if(!proxies.containsKey(proxyInterface.getName())) {
            proxies.put(proxyInterface.getName(), new ActionProxyBuilder<T>(proxyInterface).create());
        }

        return (T) proxies.get(proxyInterface.getName());
    }
}
