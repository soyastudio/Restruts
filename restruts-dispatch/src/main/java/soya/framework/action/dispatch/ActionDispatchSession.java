package soya.framework.action.dispatch;

public interface ActionDispatchSession {
    String[] parameterNames();

    Object parameterValue(String paramName);

    Object data();

}
