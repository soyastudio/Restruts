package soya.framework.action.dispatch;

public interface Evaluator {
    Object evaluate(Evaluation evaluation, Object context, Class<?> type);
}
