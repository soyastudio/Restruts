package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;
import com.google.common.base.CaseFormat;
import soya.framework.bean.DynaBean;
import soya.framework.bean.TreeNode;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;
import soya.framework.xmlbeans.XsUtils;

import java.util.LinkedHashMap;
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

    protected String variable;

    protected String sourceVariable;
    protected String sourceVariablePath;

    public String getVariable() {
        return variable;
    }

    public String getSourceVariable() {
        return sourceVariable;
    }

    public void setSourceVariable(String sourceVariable) {
        this.sourceVariable = sourceVariable;
    }

    public String getSourceVariablePath() {
        return sourceVariablePath;
    }

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

        context.arrayMap.entrySet().forEach(e -> {
            System.out.println(e.getKey());
        });
    }

    private static void annotate(ConstructContext context, TreeNode<XsNode> node, String mapping, String source) {
        if (node.getData().getNodeType().equals(XsNode.XsNodeType.Folder)) {
            return;
        }

        Construct construct = Construct.createAssignment(node, mapping, source);
        node.annotate("construct", construct);

        TreeNode<XsNode> parent = node.getParent();
        while (parent != null && parent.getAnnotation("construct") == null) {
            parent.annotate("construct", Construct.createComplexConstruct(parent, context));
            parent = parent.getParent();
        }

        // TODO:
        String token = source;
        if (token.endsWith("[*]")) {
            token = token.substring(0, token.lastIndexOf("[*]"));
        }

        if (token.contains("[*]")) {
            token = token.substring(0, token.lastIndexOf("[*]") + 3);
            Array array = context.arrayMap.get(token);
            if (array == null) {
                TreeNode<XsNode> arrayParent = findArrayParent(node);
                if (arrayParent == null) {
                    throw new RuntimeException("Cannot find array parent for: " + node.getPath());
                }

                ComplexArrayConstruct arrayConstruct = (ComplexArrayConstruct) arrayParent.getAnnotation(CONSTRUCTION_NAMESPACE);
                array = new Array(token, arrayParent);
                arrayConstruct.arrays.put(token, array);
                context.arrayMap.put(token, array);
            }

        }
    }

    private static void annotate2(ConstructContext context, TreeNode<XsNode> node, String mapping, String source) {
        Construct construct = Construct.createAssignment(node, mapping, source);
        node.annotate("construct", construct);

        TreeNode<XsNode> parent = node.getParent();
        while (parent != null && parent.getAnnotation("construct") == null) {
            parent.annotate("construct", Construct.createComplexConstruct(parent, context));
            parent = parent.getParent();
        }

        if (construct.sourceVariablePath != null) {
            if (context.arrayMap.containsKey(construct.sourceVariablePath) && construct instanceof AssignmentConstruct) {
                AssignmentConstruct assignment = (AssignmentConstruct) construct;
                assignment.setSourceVariable(context.arrayMap.get(construct.sourceVariablePath).getSourceVariable());
            } else if (!context.arrayMap.containsKey(construct.sourceVariablePath)) {
                TreeNode<XsNode> arrayParent = findArrayParent(node);

                if (arrayParent.getAnnotation(CONSTRUCTION_NAMESPACE) instanceof ComplexArrayConstruct) {
                    AssignmentConstruct assignment = (AssignmentConstruct) construct;

                    ComplexArrayConstruct arrayConstruct = (ComplexArrayConstruct) arrayParent.getAnnotation(CONSTRUCTION_NAMESPACE);

                    String name = "ARR_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, arrayConstruct.getVariable());
                    String var = arrayConstruct.getVariable();
                    if (arrayConstruct.arrays.size() > 1) {
                        name = name + (arrayConstruct.arrays.size() - 1);
                        var = var + (arrayConstruct.arrays.size() - 1);

                    } else {
                        name = name.substring(0, name.length() - 1);
                    }

                    String sourcePath = construct.sourceVariablePath;
                    String sourceVariable = sourcePath.substring(0, sourcePath.lastIndexOf("[*]"));
                    if (sourceVariable.contains(".")) {
                        sourceVariable = sourceVariable.substring(sourceVariable.lastIndexOf('.') + 1);
                    }

                    sourceVariable = "_" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sourceVariable) + "_item";

                    Array array = new Array(sourcePath, arrayParent);

                    context.arrayMap.put(array.sourcePath, array);
                    if (arrayConstruct instanceof ComplexArrayConstruct) {
                        ComplexArrayConstruct cac = (ComplexArrayConstruct) arrayConstruct;
                        cac.arrays.put(array.sourcePath, array);
                    }

                    String variable = context.arrayMap.get(assignment.sourceVariablePath).getSourceVariable();
                    assignment.setSourceVariable(variable);

                    String assign = assignment.getExpression();
                    assign = context.arrayMap.get(assignment.sourceVariablePath).getSourceVariable() + assign.substring(assign.lastIndexOf("[*]") + 3);
                }
            }

        }

    }

    private static TreeNode<XsNode> findArrayParent(TreeNode<XsNode> node) {
        TreeNode<XsNode> parent = node.getParent();
        while (parent != null && XsUtils.cardinality(parent.getData()).endsWith("-1")) {
            parent = parent.getParent();
        }

        return parent;
    }

    public static Construct createAssignment(TreeNode<XsNode> node, String mapping, String source) {
        String exp = source.trim();
        if (node.getData().getMaxOccurs() != null && node.getData().getMaxOccurs().intValue() == 1) {
            return new AssignmentConstruct(getAssignmentType(node, mapping, exp), exp);

        } else {
            return new SimpleArrayConstruct(source);
        }
    }

    public static Construct createComplexConstruct(TreeNode<XsNode> node, ConstructContext context) {
        XsNode xsNode = node.getData();
        if (XsUtils.cardinality(xsNode).endsWith("-1")) {
            return new ComplexTypeConstruct(node);

        } else {
            return new ComplexArrayConstruct(node);
        }
    }

    public static String generateESQLTemplate(XmlSchemaTree tree, String brokerSchema, String application) {

        String module = application + "_Compute";

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

    private static void printNode(TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        XsNode xsNode = node.getData();

        String name = node.getName();
        String variable = name + "_";
        String parentVariable = node.getParent().getName() + "_";

        if (namespace.equals(xsNode.getName().getNamespaceURI())) {
            name = "Abs:" + name;
        }

        if (xsNode.getNodeType().equals(XsNode.XsNodeType.Folder)) {

            builder.append("-- ", indent).appendLine(node.getPath());
            builder.append("DECLARE ", indent)
                    .append(variable)
                    .append(" REFERENCE TO ")
                    .append(parentVariable).appendLine(";");

            builder.append("CREATE LASTCHILD OF ", indent)
                    .append(parentVariable)
                    .append(" AS ")
                    .append(variable)
                    .append(" TYPE XMLNSC.Folder NAME '")
                    .append(name)
                    .appendLine("';")
                    .appendLine();

            for(TreeNode<XsNode> child : node.getChildren()) {
                printNode(child, builder, indent + 1);
            }

        } else {
            printSimpleSetting(node, builder, indent);
        }
    }

    private static void printSimpleSetting(TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        XsNode xsNode = node.getData();

        String name = node.getName();
        if(name.startsWith("@")) {
            name = name.substring(1);
        }
        String parentVariable = node.getParent().getName() + "_";

        if (namespace.equals(xsNode.getName().getNamespaceURI())) {
            name = "Abs:" + name;
        }

        builder.append("-- ", indent).appendLine(node.getPath());

        String type = xsNode.getNodeType().equals(XsNode.XsNodeType.Field)? "(XMLNSC.Field)" : "(XMLNSC.Attribute)";
        String assign = "'???'";

        builder.append("SET ", indent)
                .append(parentVariable).append(".").append(type).append(name)
                .append(" = ")
                .append(assign).appendLine(";")
                .appendLine();
    }


    public static String generateESQLTemplate(XmlSchemaTree tree) {

        ConstructContext context = (ConstructContext) tree.getAnnotation(CONSTRUCTION_CONTEXT_NAMESPACE);
        String brokerSchema = context.project.getPackageName();
        String module = context.project.getApplication() + "_Compute";

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
            printNode(context, e, builder, 2);
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

    private static void printNode(ConstructContext context, TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        if (node.getAnnotation(CONSTRUCTION_NAMESPACE) != null) {
            Object construct = node.getAnnotation(CONSTRUCTION_NAMESPACE);
            if (construct instanceof ComplexTypeConstruct) {
                printConstruction(context, node, builder, indent);

            } else if (construct instanceof ComplexArrayConstruct) {
                printComplexArray(context, node, builder, indent);

            } else if (construct instanceof SimpleArrayConstruct) {
                printSimpleArray(node, builder, indent);

            } else if (construct instanceof AssignmentConstruct) {
                printAssignment(node, builder, indent);

            }
        }
    }

    private static void printConstruction(ConstructContext context, TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        Construct construction = (Construct) node.getAnnotation(CONSTRUCTION_NAMESPACE);
        Construct parent = (Construct) node.getParent().getAnnotation(CONSTRUCTION_NAMESPACE);

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
            printNode(context, e, builder, indent + 1);
        });

    }

    private static void printComplexArray(ConstructContext context, TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        ComplexArrayConstruct construction = (ComplexArrayConstruct) node.getAnnotation(CONSTRUCTION_NAMESPACE);
        if (construction.arrays.size() == 0) {
            printConstruction(context, node, builder, indent);

        } else {
            construction.arrays.values().forEach(e -> {
                printArray(e, node, builder, indent);
            });
        }
    }

    private static void printArray(Array array, TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        final String name = "Abs:" + node.getName();
        Construct parent = (Construct) node.getParent().getAnnotation(CONSTRUCTION_NAMESPACE);
        String arrayName = array.getName();
        String item = array.getSourceVariable();

        builder.append("-- LOOP ", indent).append(array.getSourcePath()).append(" TO ").append(node.getPath()).appendLine();
        builder.append("DECLARE ", indent).append(item).append(" REFERENCE TO ").append(array.getSourcePath() + ".Item").appendLine(";");
        builder.append(arrayName, indent).append(" : WHILE LASTMOVE(").append(item).appendLine(") DO").appendLine();

        builder.append("-- ", indent + 1).appendLine(node.getPath());
        builder.append("DECLARE ", indent + 1)
                .append(node.getPath())
                .append(" REFERENCE TO ")
                .append(parent.getVariable()).appendLine(";");

        builder.append("CREATE LASTCHILD OF ", indent + 1)
                .append(parent.getVariable())
                .append(" AS ")
                .append(parent.getVariable())
                .append(" TYPE XMLNSC.Folder NAME '")
                .append(name)
                .appendLine("';")
                .appendLine();


        /*node.getChildren().forEach(e -> {
            printNode(e, builder, indent + 2);
        });*/

        builder.append("MOVE ", indent).append(item).appendLine(" NEXTSIBLING;");
        builder.append("END WHILE ", indent).append(arrayName).appendLine(";");
        builder.appendLine("-- END LOOP", indent).appendLine();
    }

    private static void printSimpleArray(TreeNode<XsNode> node, CodeBuilder builder, int indent) {

    }

    private static void printAssignment(TreeNode<XsNode> node, CodeBuilder builder, int indent) {
        AssignmentConstruct assignment = (AssignmentConstruct) node.getAnnotation(CONSTRUCTION_NAMESPACE);
        Construct parent = (Construct) node.getParent().getAnnotation(CONSTRUCTION_NAMESPACE);

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
                .append(parent.getVariable()).append(".").append(type).append(name)
                .append(" = ")
                .append(assign).appendLine(";")
                .appendLine();

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
        public ComplexTypeConstruct(TreeNode<XsNode> node) {
            this.variable = node.getName() + "_";
        }

        @Override
        public String toString() {
            return "COMPLEX(" + variable +
                    ")";
        }
    }

    public static class ComplexArrayConstruct extends Construct {
        private Map<String, Array> arrays = new LinkedHashMap<>();

        public ComplexArrayConstruct(TreeNode<XsNode> node) {
            this.variable = node.getName() + "_";
        }

        @Override
        public String toString() {
            if (arrays.size() == 0) {
                return "ARRAY()";
            } else {
                StringBuilder builder = new StringBuilder();
                arrays.values().forEach(e -> {
                    builder.append(e);
                });
                return builder.toString();
            }
        }
    }

    public static class SimpleArrayConstruct extends Construct {
        private String sourcePath;
        private String sourceVariable;

        public SimpleArrayConstruct(String sourcePath) {
            this.sourcePath = sourcePath;
            String token = sourcePath.substring(0, sourcePath.length() - 3);

            if (token.contains("[*]")) {
                this.sourceVariable = token.substring(0, token.lastIndexOf("[*]")) + "[*]";
                System.out.println("--------------- " + sourceVariable);
            }
        }

        @Override
        public String toString() {
            return "array()";
        }
    }

    public static class AssignmentConstruct extends Construct {
        private AssignmentType assignmentType;
        private String expression;

        public AssignmentConstruct(AssignmentType assignmentType, String expression) {
            this.assignmentType = assignmentType;
            this.expression = expression;

            if (AssignmentType.DIRECT.equals(assignmentType) && !expression.startsWith("$.")) {
                if (expression.contains("[*]")) {
                    int index = expression.lastIndexOf("[*]");
                    String token = expression.substring(0, +index);
                    this.sourceVariablePath = token + "[*]";
                    token = token.contains(".") ? token.substring(token.lastIndexOf('.') + 1) : token;

                } else {
                    this.expression = "$." + expression;

                }
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

        public String getAssignment() {
            if (AssignmentType.UNKNOWN.equals(assignmentType)) {
                return "???";

            } else if (AssignmentType.DEFAULT.equals(assignmentType)) {
                return expression;

            } else {
                if (sourceVariable != null) {
                    System.out.println("================= " + sourceVariable + expression.substring(sourceVariablePath.length()));
                    return sourceVariable + expression.substring(sourceVariablePath.length());
                }

                return expression;
            }
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("assign(")
                    .append(getAssignment())
                    .append(")")
                    .toString();
        }
    }

    public static class Array {

        private String sourcePath;
        private String sourceVariable;
        private String name;
        private String variable;

        public Array(String sourcePath, TreeNode<XsNode> node) {
            this.sourcePath = sourcePath;
            ComplexArrayConstruct arrayConstruct = (ComplexArrayConstruct) node.getAnnotation(CONSTRUCTION_NAMESPACE);

            this.name = "ARR_" + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, arrayConstruct.getVariable());
            this.variable = arrayConstruct.getVariable();
            if (arrayConstruct.arrays.size() > 1) {
                name = name + (arrayConstruct.arrays.size() - 1);
                variable = variable + (arrayConstruct.arrays.size() - 1);

            } else {
                name = name.substring(0, name.length() - 1);
            }

            this.sourceVariable = sourcePath.substring(0, sourcePath.lastIndexOf("[*]"));
            if (sourceVariable.contains(".")) {
                sourceVariable = sourceVariable.substring(sourceVariable.lastIndexOf('.') + 1);
            }

            sourceVariable = "_" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sourceVariable) + "_item";

            arrayConstruct.arrays.put(sourcePath, this);

        }

        public String getSourcePath() {
            return sourcePath;
        }

        public String getSourceVariable() {
            return sourceVariable;
        }

        public String getName() {
            return name;
        }

        public String getVariable() {
            return variable;
        }

        @Override
        public String toString() {
            return new StringBuilder("ARRAY(")
                    .append(sourcePath)
                    .append(",")
                    .append(sourceVariable)
                    .append(",")
                    .append(name)
                    .append(",")
                    .append(variable)
                    .append(")").toString();
        }
    }

    public enum AssignmentType {
        DEFAULT, DIRECT, UNKNOWN
    }

    public static class ConstructContext extends Construct {
        private Project project;
        private Map<String, Array> arrayMap = new LinkedHashMap<>();


        public ConstructContext(Project project) {
            this.project = project;
        }
    }
}
