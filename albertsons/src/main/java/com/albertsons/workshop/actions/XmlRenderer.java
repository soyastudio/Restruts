package com.albertsons.workshop.actions;

import com.google.common.base.CaseFormat;
import soya.framework.bean.TreeNode;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class XmlRenderer {
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

    private XmlSchemaTree tree;
    private Map<String, String> rules;

    public XmlRenderer(XmlSchemaTree tree, Map<String, String> rules) {
        this.tree = tree;
        this.rules = rules;
    }

    public String render() {
        StringBuilder builder = new StringBuilder();
        TreeNode<XsNode> root = tree.root();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<").append(root.getName()).append(" xmlns:Abs=\"https://collab.safeway.com/it/architecture/info/default.aspx\">\n");

        root.getChildren().forEach(e -> {
            render(e, builder);
        });

        builder.append("</").append(root.getName()).append(">");
        return builder.toString();
    }

    protected void render(TreeNode<XsNode> node, StringBuilder builder) {
        if(rules.containsKey(node.getPath())) {
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
        String name = DEFAULT_URI.equals(xsNode.getName().getNamespaceURI())? "Abs:" + node.getName() : node.getName();
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
        return node.getPath().replaceAll("/", "_");
    }

}
