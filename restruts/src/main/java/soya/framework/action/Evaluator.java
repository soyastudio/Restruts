package soya.framework.action;

public interface Evaluator<T> {
    Object evaluate(String expression, T context);
}
