package soya.framework.action.dispatch;

import java.io.Serializable;
import java.util.Objects;

public final class Evaluation implements Serializable {
    private final AssignmentType assignmentType;
    private final String expression;

    public Evaluation(String assignment) {
        this.assignmentType = AssignmentType.getAssignmentMethod(assignment);
        this.expression = assignment.substring(assignment.indexOf('(') + 1, assignment.lastIndexOf(')')).trim();
    }

    public Evaluation(AssignmentType assignmentType, String expression) {
        this.assignmentType = assignmentType;
        this.expression = expression;
    }

    public AssignmentType getAssignmentMethod() {
        return assignmentType;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return assignmentType.toString(expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evaluation)) return false;
        Evaluation that = (Evaluation) o;
        return assignmentType == that.assignmentType && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(assignmentType, expression);
    }
}
