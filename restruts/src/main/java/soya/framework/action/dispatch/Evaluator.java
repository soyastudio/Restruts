package soya.framework.action.dispatch;

public interface Evaluator {
    Object evaluate(Assignment assignment, Object context, Class<?> type);
}
