package com.albertsons.workshop.actions;

import soya.framework.bean.TreeNode;
import soya.framework.xmlbeans.XsNode;

public abstract class Construct {

    private String type;
    private String dataType;
    private String assignment;

    public static Construct createAssignment(TreeNode<XsNode> node, String mapping, String source) {
        if (node.getData().getMaxOccurs() != null && node.getData().getMaxOccurs().intValue() == 1) {
            return new AssignmentConstruct(getAssignmentType(node, mapping, source), source.trim());

        } else {
            return new SimpleArrayConstruct();

        }
    }

    public static Construct createComplexConstruct(TreeNode<XsNode> node) {
        return new ComplexTypeConstruct(node);
    }

    private static AssignmentType getAssignmentType(TreeNode<XsNode> node, String mapping, String source) {
        if (mapping == null) {
            return AssignmentType.UNKNOWN;
        } else {
            String token = mapping.trim().toUpperCase();
            if (token.contains("DEFAULT")) {
                return AssignmentType.DEFAULT;

            } else if (token.contains("DIRECT")) {
                return AssignmentType.DIRECT;

            } else {
                return AssignmentType.UNKNOWN;
            }
        }
    }

    public static class ComplexTypeConstruct extends Construct {
        private String var;

        public ComplexTypeConstruct(TreeNode<XsNode> node) {
            this.var = node.getName() + "_";
        }

        @Override
        public String toString() {
            return "complex(" + var +
                    ")";
        }
    }

    public static class ComplexArrayConstruct extends Construct {

        @Override
        public String toString() {
            return "array()";
        }
    }

    public static class SimpleArrayConstruct extends Construct {
        @Override
        public String toString() {
            return "array()";
        }
    }

    public static class AssignmentConstruct extends Construct {
        private AssignmentType assignmentType;
        private String expression;
        private String description;

        public AssignmentConstruct(AssignmentType assignmentType, String expression) {
            this.assignmentType = assignmentType;
            this.expression = expression;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("assign(");
            if(assignmentType.equals(AssignmentType.DEFAULT)) {
                builder.append("'").append(expression).append("'");
            } else if(assignmentType.equals(AssignmentType.DIRECT)) {
                builder.append(expression);
            } else {
                builder.append("???");
            }

            builder.append(")");
            return builder.toString();
        }
    }

    public enum AssignmentType {
        DEFAULT, DIRECT, CONDITION, UNKNOWN
    }
}
