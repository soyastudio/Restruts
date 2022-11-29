package soya.framework.action.orchestration;

import soya.framework.action.ConvertUtils;
import soya.framework.action.Resources;
import soya.framework.action.dispatch.Evaluation;
import soya.framework.action.dispatch.AssignmentType;
import soya.framework.action.dispatch.Evaluator;

import java.io.InputStream;

public class ProcessSessionEvaluator implements Evaluator {
    @Override
    public Object evaluate(Evaluation evaluation, Object context, Class<?> type) {
        ProcessSession session = (ProcessSession) context;

        Object value = null;

        AssignmentType assignmentType = evaluation.getAssignmentMethod();
        String expression = evaluation.getExpression();
        if (AssignmentType.VALUE.equals(assignmentType)) {
            value = ConvertUtils.convert(expression, type);

        } else if (AssignmentType.RESOURCE.equals(assignmentType)) {
            if (InputStream.class.isAssignableFrom(type)) {
                value = Resources.getResourceAsInputStream(expression);

            } else {
                value = ConvertUtils.convert(Resources.getResourceAsString(expression), type);

            }

        } else if (AssignmentType.REFERENCE.equals(assignmentType)) {
            value = ConvertUtils.convert(session.parameterValue(evaluation.getExpression()), type);

        } else if (AssignmentType.PARAMETER.equals(assignmentType)) {
            value = ConvertUtils.convert(session.get(evaluation.getExpression()), type);
        }

        return value;
    }
}
