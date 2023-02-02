package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;
import soya.framework.bean.TreeNode;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;

import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

public class JavaCodeGenerator {
    private static String DEFAULT_URI = "http://collab.safeway.com/it/architecture/info/default.aspx";

    private static String[] indents = new String[]{
            "",
            "\t",
            "\t\t",
            "\t\t\t",
            "\t\t\t\t",
            "\t\t\t\t\t",
            "\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
    };

    private Project project;
    private XmlSchemaTree tree;
    private Map<String, String> rules;

    private String className;

    public JavaCodeGenerator(Project project, XmlSchemaTree tree, Map<String, String> rules) {
        this.project = project;
        this.tree = tree;
        this.rules = rules;

        this.className = project.getName() + "Transformer";
    }

    public String render() {

        StringBuilder builder = new StringBuilder();

        builder.append("package ").append(project.getPackageName()).append(";").append("\n\n");

        builder.append("public class ").append(className).append(" {\n");

        createMainMethod(builder);


        builder.append("}");
        return builder.toString();
    }

    protected void createMainMethod(StringBuilder builder) {
        TreeNode<XsNode> root = tree.root();
        builder.append(indents[1]).append("public String transform(String json) {").append("\n");

        builder.append(indents[2]).append("StringBuilder builder = new StringBuilder();").append("\n");


        root.getChildren().forEach(e -> {
            String methodName = methodName(e);
            builder.append(methodName).append("();\n");
        });

        builder.append(indents[2]).append("return builder.toString();").append("\n");
        builder.append(indents[1]).append("}\n");

        root.getChildren().forEach(e -> {
            createSubMethod(e, builder);
        });
    }

    protected void createSubMethod(TreeNode<XsNode> node, StringBuilder builder) {
        String methodName = methodName(node);
        builder.append("\n");
        builder.append(indents[1]).append("// ").append(node.getPath()).append("\n");
        builder.append(indents[1]).append("protected void ").append(methodName).append("() {").append("\n");

        node.getChildren().forEach(e -> {
            String sub = methodName(e);
            builder.append(sub).append("();\n");
        });

        builder.append(indents[1]).append("}\n");

        node.getChildren().forEach(e -> {
            createSubMethod(e, builder);
        });
    }

    protected void render(TreeNode<XsNode> node, StringBuilder builder) {
        if (rules.containsKey(node.getPath())) {

            XsNode xsNode = node.getData();
            if (xsNode.getNodeType().equals(XsNode.XsNodeType.Folder)) {
                renderFolder(node, builder);

            } else if (xsNode.getNodeType().equals(XsNode.XsNodeType.Field)) {
                renderField(node, builder);
            }
        }
    }

    protected void renderFolder(TreeNode<XsNode> node, StringBuilder builder) {
        XsNode xsNode = node.getData();
        String name = DEFAULT_URI.equals(xsNode.getName().getNamespaceURI()) ? "Abs:" + node.getName() : node.getName();
        builder.append(indents[indent(node)]).append("<").append(name).append(">\n");
        if (xsNode.getSchemaType().getAttributeProperties() != null && xsNode.getSchemaType().getAttributeProperties().length > 0) {
            Arrays.stream(xsNode.getSchemaType().getAttributeProperties()).forEach(attr -> {
                // System.out.println(node.getPath() + "/@" + attr.getName());
            });
        }

        node.getChildren().forEach(c -> {
            render(c, builder);
        });

        builder.append(indents[indent(node)]).append("</").append(name).append(">\n");
    }

    protected void renderField(TreeNode<XsNode> node, StringBuilder builder) {

        builder.append(indents[indent(node)]).append("<Abs:").append(node.getName()).append(">");
        builder.append("VALUE");
        builder.append("</Abs:").append(node.getName()).append(">\n");
    }

    protected int indent(TreeNode<XsNode> node) {
        return new StringTokenizer(node.getPath(), "/").countTokens() - 1;
    }

    protected String methodName(TreeNode node) {
        return node.getPath().replaceAll("/", "_").replace("@", "");
    }

    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_DistinctId() {

    }

}
