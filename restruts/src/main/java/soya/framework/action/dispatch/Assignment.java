package soya.framework.action.dispatch;

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

    @Override
    public String toString() {
        return assignmentMethod.toString(expression);
    }
}
