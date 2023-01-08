package soya.framework.action.dispatch;

public interface Resolver {
    Object resolve(String expression, Object context);
}
