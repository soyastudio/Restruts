package soya.framework.action.dispatch.proxy;

public class ActionProxyBuildException extends RuntimeException {

    public ActionProxyBuildException(String message) {
        super(message);
    }

    public ActionProxyBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionProxyBuildException(Throwable cause) {
        super(cause);
    }
}
