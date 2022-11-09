package soya.framework.action.dispatch;

import java.io.Serializable;
import java.util.Objects;

public final class Evaluation implements Serializable {
    private final EvaluationMethod evaluationMethod;
    private final String expression;

    public Evaluation(String assignment) {
        this.evaluationMethod = EvaluationMethod.getAssignmentMethod(assignment);
        this.expression = assignment.substring(assignment.indexOf('(') + 1, assignment.lastIndexOf(')')).trim();
    }

    public Evaluation(EvaluationMethod evaluationMethod, String expression) {
        this.evaluationMethod = evaluationMethod;
        this.expression = expression;
    }

    public EvaluationMethod getAssignmentMethod() {
        return evaluationMethod;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return evaluationMethod.toString(expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evaluation)) return false;
        Evaluation that = (Evaluation) o;
        return evaluationMethod == that.evaluationMethod && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaluationMethod, expression);
    }
}
