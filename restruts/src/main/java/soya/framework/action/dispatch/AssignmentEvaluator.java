package soya.framework.action.dispatch;

import soya.framework.action.ConvertUtils;
import soya.framework.action.Resources;

import java.io.InputStream;

public abstract class AssignmentEvaluator {

    public Object evaluate(Assignment assignment, Object context, Class<?> type) {
        Object value = null;

        AssignmentType assignmentType = assignment.getAssignmentType();
        String expression = assignment.getExpression();
        if (AssignmentType.VALUE.equals(assignmentType)) {
            value = ConvertUtils.convert(expression, type);

        } else if (AssignmentType.RESOURCE.equals(assignmentType)) {
            if (InputStream.class.isAssignableFrom(type)) {
                value = Resources.getResourceAsInputStream(expression);

            } else {
                value = ConvertUtils.convert(Resources.getResourceAsString(expression), type);

            }

        } else if (AssignmentType.REFERENCE.equals(assignmentType)) {
            value = fromReference(expression, context, type);

        } else if (AssignmentType.PARAMETER.equals(assignmentType)) {
            value = fromParameter(expression, context, type);
        }

        return value;
    }

    protected abstract Object fromParameter(String exp, Object context, Class<?> type);

    protected abstract Object fromReference(String exp, Object context, Class<?> type);

}
