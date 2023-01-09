package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;
import soya.framework.bean.DynaBean;
import soya.framework.bean.TreeNode;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Construct {
    public static final String CONSTRUCTION_CONTEXT_NAMESPACE = "context";
    public static final String CONSTRUCTION_NAMESPACE = "construct";

    private static String namespace = "http://collab.safeway.com/it/architecture/info/default.aspx";

    private static String PATTERN_CAST_DECIMAL = "CAST(%s AS DECIMAL(10, 2))";

    private static String inputRootVariable = "_inputRootNode";
    private static String inputRootReference = "InputRoot.JSON.Data";

    private static String outputRootName;
    private static String outputRootVariable;

    public static void annotate(Project project, XmlSchemaTree tree, List<DynaBean> annotations) {
        ConstructContext context = new ConstructContext(project);
        tree.annotate(CONSTRUCTION_CONTEXT_NAMESPACE, context);
        annotations.forEach(e -> {
            if (e.get("Target") != null) {
                String target = e.getAsString("Target");
                String rule = e.getAsString("Mapping");
                String source = e.getAsString("Source");

                if (e.get("Target") != null && e.get("Source") != null) {
                    if (tree.contains(target)) {
                        annotate(context, tree.get(target), e.getAsString("Mapping"), e.get("Source").toString());
                    }
                }

            }
        });
    }

    private static void annotate(ConstructContext context, TreeNode<XsNode> node, String mapping, String source) {
        node.annotate("construct", Construct.createAssignment(node, mapping, source));


        TreeNode<XsNode> parent = node.getParent();
        while (parent != null && parent.getAnnotation("construct") == null) {
            parent.annotate("construct", Construct.createComplexConstruct(parent, context));

            parent = parent.getParent();
        }
    }

    public static String generateESQLTemplate(XmlSchemaTree tree, Project project) {

        String brokerSchema = project.getPackageName();
        String module = project.getApplication() + "_Compute";

        outputRootName = tree.root().getName();
        outputRootVariable = outputRootName + "_";

        outputRootName = tree.root().getName();
        outputRootVariable = outputRootName + "_";

        CodeBuilder builder = CodeBuilder.newInstance();
        if (brokerSchema != null && brokerSchema.trim().length() > 0) {
            builder.append("BROKER SCHEMA ").append(brokerSchema.trim()).append("\n\n");
        }
        builder.append("CREATE COMPUTE MODULE ").appendLine(module);
        builder.appendLine();

        // UDP:
        builder.appendLine("-- Declare UDPs", 1);
        builder.appendLine("DECLARE VERSION_ID EXTERNAL CHARACTER '1.0.0';", 1);
        builder.appendLine("DECLARE SYSTEM_ENVIRONMENT_CODE EXTERNAL CHARACTER 'PROD';", 1);

        builder.appendLine();

        // Namespace
        declareNamespace(builder);

        builder.appendLine("CREATE FUNCTION Main() RETURNS BOOLEAN", 1);
        begin(builder, 1);

        // Declare Input Root
        builder.appendLine("-- Declare Input Message Root", 2);
        builder.appendLine("DECLARE " + inputRootVariable + " REFERENCE TO " + inputRootReference + ";", 2);
        builder.appendLine();

        // Declare Output Domain
        builder.appendLine("-- Declare Output Message Root", 2);
        builder.append("CREATE LASTCHILD OF OutputRoot DOMAIN ", 2).append("'XMLNSC'").appendLine(";").appendLine();

        builder.append("DECLARE ", 2).append(outputRootVariable).append(" REFERENCE TO OutputRoot.XMLNSC.").append(outputRootName).appendLine(";");
        builder.append("CREATE LASTCHILD OF OutputRoot.", 2).append("XMLNSC AS ").append(outputRootVariable).append(" TYPE XMLNSC.Folder NAME '").append(outputRootName).append("'").appendLine(";");
        builder.append("SET OutputRoot.XMLNSC.", 2).append(outputRootName).appendLine(".(XMLNSC.NamespaceDecl)xmlns:Abs=Abs;");
        builder.appendLine();

        //print node:
        tree.root().getChildren().forEach(e -> {
            printNode(e, builder, 2);
        });

        // closing
        builder.appendLine("RETURN TRUE;", 2);
        builder.appendLine("END;", 1);
        builder.appendLine();
        builder.appendLine("END MODULE;");

        return builder.toString();
    }

    private static void declareNamespace(CodeBuilder builder) {
        builder.appendLine("-- Declare Namespace", 1);
        builder.appendLine("DECLARE " + "Abs" + " NAMESPACE " + "'" + namespace + "'" + ";", 1);
        builder.appendLine();
    }

    private static void begin(CodeBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append("\t");
        }

        builder.append("BEGIN").append("\n");
    }

    private static void printNode(TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        if (node.getAnnotation(CONSTRUCTION_NAMESPACE) != null) {
            Object construct = node.getAnnotation(CONSTRUCTION_NAMESPACE);
            if (construct instanceof ComplexTypeConstruct) {
                printConstruction(node, builder, indent);

            } else if (construct instanceof ComplexArrayConstruct) {
                printComplexArray(node, builder, indent);

            } else if (construct instanceof SimpleArrayConstruct) {
                printSimpleArray(node, builder, indent);

            } else if (construct instanceof AssignmentConstruct) {
                printAssignment(node, builder, indent);

            }
        }
    }

    private static void printConstruction(TreeNode<XsNode> node, CodeBuilder builder, int indent) {

        ComplexTypeConstruct construction = (ComplexTypeConstruct) node.getAnnotation(CONSTRUCTION_NAMESPACE);

        ComplexTypeConstruct parent = (ComplexTypeConstruct) node.getParent().getAnnotation(CONSTRUCTION_NAMESPACE);
        String name = node.getName();
        if (namespace.equals(node.getData().getName().getNamespaceURI())) {
            name = "Abs:" + name;
        }

        builder.append("-- ", indent).appendLine(node.getPath());
        builder.append("DECLARE ", indent)
                .append(construction.getVariable())
                .append(" REFERENCE TO ")
                .append(parent.getVariable()).appendLine(";");

        builder.append("CREATE LASTCHILD OF ", indent)
                .append(parent.getVariable())
                .append(" AS ")
                .append(construction.getVariable())
                .append(" TYPE XMLNSC.Folder NAME '")
                .append(name)
                .appendLine("';")
                .appendLine();

        node.getChildren().forEach(e -> {
            printNode(e, builder, indent + 1);
        });

    }

    private static void printComplexArray(TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        ComplexArrayConstruct construction = (ComplexArrayConstruct) node.getAnnotation(CONSTRUCTION_NAMESPACE);

        Object parent = node.getParent().getAnnotation(CONSTRUCTION_NAMESPACE);
        String parentVar = null;
        if(parent instanceof ComplexTypeConstruct) {
            parentVar = ((ComplexTypeConstruct) parent).getVariable();
        } else {
            parentVar = ((ComplexArrayConstruct) parent).getVariable();
        }
        String name = node.getName();
        if (namespace.equals(node.getData().getName().getNamespaceURI())) {
            name = "Abs:" + name;
        }

        builder.append("-- ", indent).appendLine(node.getPath());
        builder.append("DECLARE ", indent)
                .append(construction.getVariable())
                .append(" REFERENCE TO ")
                .append(parentVar).appendLine(";");

        builder.append("CREATE LASTCHILD OF ", indent)
                .append(parentVar)
                .append(" AS ")
                .append(construction.getVariable())
                .append(" TYPE XMLNSC.Folder NAME '")
                .append(name)
                .appendLine("';")
                .appendLine();

        node.getChildren().forEach(e -> {
            printNode(e, builder, indent + 1);
        });
    }

    private static void printSimpleArray(TreeNode<XsNode> node, CodeBuilder builder, int indent) {

    }

    private static void printAssignment(TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        AssignmentConstruct assignment = (AssignmentConstruct) node.getAnnotation(CONSTRUCTION_NAMESPACE);
        ComplexTypeConstruct construction = (ComplexTypeConstruct) node.getParent().getAnnotation(CONSTRUCTION_NAMESPACE);

        builder.append("-- ", indent).appendLine(node.getPath());

        String type = "(XMLNSC.Field)";
        String name = node.getName();
        String assign = "'???'";
        if (assignment.getAssignmentType().equals(AssignmentType.DEFAULT)) {
            assign = assignment.getExpression();

        } else if (assignment.getAssignmentType().equals(AssignmentType.DIRECT)) {
            assign = assignment.getExpression().replace("$.", inputRootVariable + ".");
        }

        builder.append("SET ", indent)
                .append(construction.getVariable()).append(".").append(type).append(name)
                .append(" = ")
                .append(assign).appendLine(";")
                .appendLine();

    }

    public static Construct createAssignment(TreeNode<XsNode> node, String mapping, String source) {
        String exp = source.trim();
        if (node.getData().getMaxOccurs() != null && node.getData().getMaxOccurs().intValue() == 1) {
            return new AssignmentConstruct(getAssignmentType(node, mapping, exp), exp);

        } else {
            return new SimpleArrayConstruct();

        }
    }

    public static Construct createComplexConstruct(TreeNode<XsNode> node, ConstructContext context) {
        return new ComplexTypeConstruct(node);
    }

    private static AssignmentType getAssignmentType(TreeNode<XsNode> node, String mapping, String source) {
        if (mapping == null) {
            return AssignmentType.UNKNOWN;

        } else if (source.contains(" ") || source.contains("\n")) {
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
        private String variable;

        public ComplexTypeConstruct(TreeNode<XsNode> node) {
            this.variable = node.getName() + "_";
        }

        public String getVariable() {
            return variable;
        }

        @Override
        public String toString() {
            return "complex(" + variable +
                    ")";
        }
    }

    public static class ComplexArrayConstruct extends Construct {
        private String variable;

        public ComplexArrayConstruct(TreeNode<XsNode> node) {
            this.variable = node.getName() + "_";
        }

        public String getVariable() {
            return variable;
        }

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
            if (AssignmentType.DIRECT.equals(assignmentType) && !expression.startsWith("$.")) {
                this.expression = "$." + expression;
            } else {
                this.expression = expression;

            }
        }

        public AssignmentType getAssignmentType() {
            return assignmentType;
        }

        public String getExpression() {
            return expression;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("assign(");
            if (assignmentType.equals(AssignmentType.DEFAULT)) {
                builder.append("'").append(expression).append("'");
            } else if (assignmentType.equals(AssignmentType.DIRECT)) {
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

    public static class ConstructContext extends Construct {
        private Project project;
        private Map<String, String> sourceVariables = new HashMap<>();

        public ConstructContext(Project project) {
            this.project = project;
        }
    }
}
