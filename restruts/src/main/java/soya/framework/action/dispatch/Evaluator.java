package soya.framework.action.dispatch;

public interface Evaluator<T> {
    Object evaluate(String expression, T context);
}
