package soya.framework.action.orchestration;

import soya.framework.action.dispatch.Assignment;
import soya.framework.action.dispatch.Evaluator;

import java.io.Serializable;
import java.util.Objects;

public class Condition implements Serializable {
    private static final Evaluator evaluator = new ProcessSessionEvaluator();

    private final Assignment left;
    private final Assignment right;
    private final Operator operator;

    private Condition(Assignment left, Assignment right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public boolean evaluate(ProcessSession session) {
        String leftValue = (String) evaluator.evaluate(left, session, String.class);
        String rightValue = (String) evaluator.evaluate(left, session, String.class);

        switch (operator) {
            case EQUAL_TO:
                return leftValue.equals(rightValue);

            case NOT_EQUAL_TO:
                return !leftValue.equals(rightValue);

            case LARGER_THAN:
                return Double.parseDouble(leftValue) > Double.parseDouble(rightValue);

            case NOT_LARGER_THAN:
                return Double.parseDouble(leftValue) <= Double.parseDouble(rightValue);

            case LESS_THAN:
                return Double.parseDouble(leftValue) < Double.parseDouble(rightValue);

            case NOT_LESS_THAN:
                return Double.parseDouble(leftValue) >= Double.parseDouble(rightValue);

            case AND:
                return and(Boolean.parseBoolean(leftValue), Boolean.parseBoolean(rightValue));

            case OR:
                return or(Boolean.parseBoolean(leftValue), Boolean.parseBoolean(rightValue));

            case NANT:
                return nand(Boolean.parseBoolean(leftValue), Boolean.parseBoolean(rightValue));

            case NOR:
                return !(Boolean.parseBoolean(leftValue) || Boolean.parseBoolean(rightValue));

            case XOR:
                return xnor(Boolean.parseBoolean(leftValue), Boolean.parseBoolean(rightValue));

        }

        return false;
    }

    private boolean and(boolean a, boolean b) {
        return a && b;
    }

    private boolean nand(boolean a, boolean b) {
        return !(a && b);
    }

    private boolean or(boolean a, boolean b) {
        return a || b;
    }

    private boolean nor(boolean a, boolean b) {
        return !(a || b);
    }

    private boolean xor(boolean a, boolean b) {
        return (!a && b) || (!b && a);
    }

    private boolean xnor(boolean a, boolean b) {
        return (a && b) || (!a && !b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Condition)) return false;
        Condition condition = (Condition) o;
        return Objects.equals(left, condition.left) && Objects.equals(right, condition.right) && operator == condition.operator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, operator);
    }

    public static Condition create(String expression) {
        // TODO:
        throw new UnsupportedOperationException("TODO");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Assignment left;
        private Assignment right;
        private Operator operator;

        private Builder() {
        }

        public Builder left(Assignment assignment) {
            this.left = assignment;
            return this;
        }

        public Builder right(Assignment assignment) {
            this.right = assignment;
            return this;
        }

        public Builder equalTo() {
            this.operator = Operator.EQUAL_TO;
            return this;
        }

        public Builder notEqualTo() {
            this.operator = Operator.NOT_EQUAL_TO;
            return this;
        }

        public Builder largerThan() {
            this.operator = Operator.LARGER_THAN;
            return this;
        }

        public Builder notLargerThan() {
            this.operator = Operator.NOT_LARGER_THAN;
            return this;
        }

        public Builder lessThan() {
            this.operator = Operator.LESS_THAN;
            return this;
        }

        public Builder notLessThan() {
            this.operator = Operator.NOT_LESS_THAN;
            return this;
        }

        public Builder and() {
            this.operator = Operator.AND;
            return this;
        }

        public Builder or() {
            this.operator = Operator.OR;
            return this;
        }

        public Builder nand() {
            this.operator = Operator.NANT;
            return this;
        }

        public Builder nor() {
            this.operator = Operator.NOR;
            return this;
        }

        public Builder xor() {
            this.operator = Operator.XOR;
            return this;
        }

        public Builder xnor() {
            this.operator = Operator.XNOR;
            return this;
        }

        public Condition create() {
            if (left == null) {
                throw new IllegalArgumentException("Left assignment is not set.");
            }

            if (right == null) {
                throw new IllegalArgumentException("Right assignment is not set.");

            }

            if (operator == null) {
                throw new IllegalArgumentException("Operator is not set.");
            }

            return new Condition(left, right, operator);
        }
    }

    enum Operator {
        EQUAL_TO,
        NOT_EQUAL_TO,
        LARGER_THAN,
        NOT_LARGER_THAN,
        LESS_THAN,
        NOT_LESS_THAN,
        AND,
        NANT,
        OR,
        NOR,
        XOR,
        XNOR
    }

}
