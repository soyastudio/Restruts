package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;

import com.google.gson.*;
import soya.framework.bean.TreeNode;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class JsonCodeGenerator {

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
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Project project;
    private XmlSchemaTree tree;
    private Map<String, String> rules;

    public JsonCodeGenerator(Project project, XmlSchemaTree tree, Map<String, String> rules) {
        this.project = project;
        this.tree = tree;
        this.rules = rules;
    }

    public String render() {
        JsonObject jsonObject = transform();

        //return GSON.toJson(jsonObject);

        return toXml(jsonObject);
    }

    protected JsonObject transform() {
        JsonObject jsonObject = new JsonObject();
        TreeNode<XsNode> root = tree.root();
        JsonObject o = new JsonObject();
        root.getChildren().forEach(e -> {
            addChild(e, o);
        });

        jsonObject.add(root.getName(), o);

        return jsonObject;
    }

    protected void addChild(TreeNode<XsNode> node, JsonObject parent) {
        XsNode xsNode = node.getData();
        if(xsNode.getNodeType().equals(XsNode.XsNodeType.Folder)) {

            if(xsNode.getMaxOccurs() != null && xsNode.getMaxOccurs().equals(BigInteger.ONE)) {
                JsonObject object = new JsonObject();
                node.getChildren().forEach(e -> {
                    addChild(e, object);
                });

                parent.add(node.getName(), object);

            } else {
                JsonArray array = new JsonArray();

                JsonObject ob = new JsonObject();
                node.getChildren().forEach(e -> {
                    addChild(e, ob);
                });
                array.add(ob);

                parent.add(node.getName(), array);
            }

        } else if(xsNode.getNodeType().equals(XsNode.XsNodeType.Field)) {
            if(xsNode.getMaxOccurs() != null && xsNode.getMaxOccurs().equals(BigInteger.ONE)) {
                JsonPrimitive primitive = new JsonPrimitive(node.getName().toUpperCase(Locale.ROOT));
                parent.add(node.getName(), primitive);
            } else {
                JsonArray array = new JsonArray();

                JsonPrimitive primitive = new JsonPrimitive(node.getName().toUpperCase(Locale.ROOT));
                array.add(primitive);

                parent.add(node.getName(), array);
            }

        } else {
            JsonPrimitive primitive = new JsonPrimitive(node.getName().toUpperCase(Locale.ROOT));
            parent.add(node.getName(), primitive);
        }
    }

    protected String toXml(JsonObject jsonObject) {
        Map.Entry<String, JsonElement> entry = getRoot(jsonObject);
        String rootName = entry.getKey();
        JsonObject root = entry.getValue().getAsJsonObject();

        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
        builder.append("<").append(rootName).append(" xmlns:Abs=\"https://collab.safeway.com/it/architecture/info/default.aspx\">").append("\n");

        root.entrySet().forEach(e -> {
            if(e.getKey().equals("DocumentData")) {
                printHeader(project, builder);

            } else {
                printPayload(e.getKey(), e.getValue().getAsJsonObject(), builder);
            }
        });

        builder.append("</").append(rootName).append(">");

        return builder.toString();
    }

    private void printHeader(Project project, StringBuilder builder) {
        builder.append(indents[1]).append("<DocumentData>").append("\n");

        builder.append(indents[2]).append("<Document SystemEnvironmentCd=\"PROD\" VersionId=\"2.9.1.061\">").append("\n");

        builder.append(indents[2]).append("</Document>").append("\n");


        // DocumentAction
        builder.append(indents[2]).append("<DocumentAction>").append("\n");
        builder.append(indents[3]).append("<Abs:ActionTypeCd>UPDATE</Abs:ActionTypeCd>").append("\n");
        builder.append(indents[3]).append("<Abs:RecordTypeCd>CHANGE</Abs:RecordTypeCd>").append("\n");
        builder.append(indents[2]).append("</DocumentAction>").append("\n");

        builder.append(indents[1]).append("</DocumentData>").append("\n");
    }

    private void printPayload(String name, JsonObject data, StringBuilder builder) {
        builder.append(indents[1]).append("<").append(name).append(">").append("\n");
        data.entrySet().forEach(e -> {
            printData(new Node(e.getKey(), e.getValue(), 2), builder);
        });

        builder.append(indents[1]).append("</").append(name).append(">").append("\n");
    }

    private void printData(Node node, StringBuilder builder) {
        if(node.element.isJsonObject()) {
            printJsonObject(node, builder);

        } else if(node.element.isJsonArray()) {
            printJsonArray(node, builder);

        } else {
            printJsonPrimitive(node, builder);
        }
    }

    private void printJsonObject(Node node, StringBuilder builder) {
        builder.append(indents[node.indent]).append("<").append(node.getTagName()).append(">").append("\n");

        JsonObject object = node.element.getAsJsonObject();
        int indent = node.indent + 1;
        object.entrySet().forEach(e -> {
            printData(new Node(e.getKey(), e.getValue(), indent), builder);
        });

        builder.append(indents[node.indent]).append("</").append(node.getTagName()).append(">").append("\n");
    }

    private void printJsonArray(Node node, StringBuilder builder) {
        JsonArray array = node.element.getAsJsonArray();
        array.forEach(e -> {
            if(e.isJsonObject()) {
                printJsonObject(new Node(node.name, e, node.indent), builder);
            } else if (e.isJsonPrimitive()) {
                printJsonPrimitive(new Node(node.name, e, node.indent), builder);
            }
        });
    }

    private void printJsonPrimitive(Node node, StringBuilder builder) {
        builder.append(indents[node.indent]).append("<").append(node.getTagName()).append(">");
        builder.append(node.getElement().getAsJsonPrimitive().getAsString());
        builder.append("</").append(node.getTagName()).append(">").append("\n");
    }

    private Map.Entry<String, JsonElement> getRoot(JsonObject jsonObject) {
        Iterator<Map.Entry<String, JsonElement>> iterator = jsonObject.entrySet().iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }

        throw new IllegalArgumentException();
    }

    static class Node {
        private final String name;
        private final JsonElement element;
        private final int indent;

        public Node(String name, JsonElement element, int indent) {
            this.name = name;
            this.element = element;
            this.indent = indent;
        }

        public String getName() {
            return name;
        }

        public JsonElement getElement() {
            return element;
        }

        public int getIndent() {
            return indent;
        }

        public String getTagName() {
            return "Abs:" + name;
        }
    }
}
