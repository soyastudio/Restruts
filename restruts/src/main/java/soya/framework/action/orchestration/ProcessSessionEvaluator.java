package soya.framework.action.orchestration;

import soya.framework.action.ConvertUtils;
import soya.framework.action.Resources;
import soya.framework.action.dispatch.Assignment;
import soya.framework.action.dispatch.AssignmentMethod;
import soya.framework.action.dispatch.Evaluator;

import java.io.InputStream;

public class ProcessSessionEvaluator implements Evaluator {
    @Override
    public Object evaluate(Assignment assignment, Object context, Class<?> type) {
        ProcessSession session = (ProcessSession) context;

        Object value = null;

        AssignmentMethod assignmentMethod = assignment.getAssignmentMethod();
        String expression = assignment.getExpression();
        if (AssignmentMethod.VALUE.equals(assignmentMethod)) {
            value = ConvertUtils.convert(expression, type);

        } else if (AssignmentMethod.RESOURCE.equals(assignmentMethod)) {
            if (InputStream.class.isAssignableFrom(type)) {
                value = Resources.getResourceAsInputStream(expression);

            } else {
                value = ConvertUtils.convert(Resources.getResourceAsString(expression), type);

            }

        } else if (AssignmentMethod.REFERENCE.equals(assignmentMethod)) {
            value = ConvertUtils.convert(session.parameterValue(assignment.getExpression()), type);

        } else if (AssignmentMethod.PARAMETER.equals(assignmentMethod)) {
            value = ConvertUtils.convert(session.get(assignment.getExpression()), type);
        }

        return value;
    }
}
