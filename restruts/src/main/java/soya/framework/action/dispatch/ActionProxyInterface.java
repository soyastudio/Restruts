package soya.framework.action.dispatch;

public class ActionProxyInterface {
    private String packageName;
    private String className;
    private ProxyMethod[] methods;

    static class ProxyMethod {
        private String name;
        private String returnType;
        private String dispatchURI;
    }
}
