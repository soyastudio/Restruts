package soya.framework.action.orchestration;

import soya.framework.action.ConvertUtils;
import soya.framework.action.Resources;
import soya.framework.action.dispatch.Evaluation;
import soya.framework.action.dispatch.EvaluationMethod;
import soya.framework.action.dispatch.Evaluator;

import java.io.InputStream;

public class ProcessSessionEvaluator implements Evaluator {
    @Override
    public Object evaluate(Evaluation evaluation, Object context, Class<?> type) {
        ProcessSession session = (ProcessSession) context;

        Object value = null;

        EvaluationMethod evaluationMethod = evaluation.getAssignmentMethod();
        String expression = evaluation.getExpression();
        if (EvaluationMethod.VALUE.equals(evaluationMethod)) {
            value = ConvertUtils.convert(expression, type);

        } else if (EvaluationMethod.RESOURCE.equals(evaluationMethod)) {
            if (InputStream.class.isAssignableFrom(type)) {
                value = Resources.getResourceAsInputStream(expression);

            } else {
                value = ConvertUtils.convert(Resources.getResourceAsString(expression), type);

            }

        } else if (EvaluationMethod.REFERENCE.equals(evaluationMethod)) {
            value = ConvertUtils.convert(session.parameterValue(evaluation.getExpression()), type);

        } else if (EvaluationMethod.PARAMETER.equals(evaluationMethod)) {
            value = ConvertUtils.convert(session.get(evaluation.getExpression()), type);
        }

        return value;
    }
}
