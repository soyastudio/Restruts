package soya.framework.action.dispatch;

import org.checkerframework.checker.units.qual.A;
import soya.framework.action.ConvertUtils;
import soya.framework.action.Resources;

import java.io.IOException;
import java.io.Serializable;

public final class Assignment implements Serializable {
    private final AssignmentMethod assignmentMethod;
    private final String expression;

    public Assignment(String assignment) {
        this.assignmentMethod = AssignmentMethod.getAssignmentMethod(assignment);
        this.expression = assignment.substring(assignment.indexOf('(') + 1, assignment.lastIndexOf(')')).trim();
    }

    public Assignment(AssignmentMethod assignmentMethod, String expression) {
        this.assignmentMethod = assignmentMethod;
        this.expression = expression;
    }

    public AssignmentMethod getAssignmentMethod() {
        return assignmentMethod;
    }

    public String getExpression() {
        return expression;
    }

    public <T> T evaluate(Object context, Evaluator evaluator, Class<T> type) throws IOException {
        Object v = null;
        if(AssignmentMethod.VALUE.equals(assignmentMethod)) {
            v = expression;

        } else if(AssignmentMethod.RESOURCE.equals(assignmentMethod)) {
            v = Resources.getResourceAsString(expression);

        } else {
            v = evaluator.evaluate(expression, context);
        }

        return (T) ConvertUtils.convert(v, type);
    }

    @Override
    public String toString() {
        return assignmentMethod.toString(expression);
    }
}
