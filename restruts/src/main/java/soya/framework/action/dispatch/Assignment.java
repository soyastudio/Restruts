package soya.framework.action.dispatch;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment)) return false;
        Assignment that = (Assignment) o;
        return assignmentMethod == that.assignmentMethod && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentMethod, expression);
    }
}
