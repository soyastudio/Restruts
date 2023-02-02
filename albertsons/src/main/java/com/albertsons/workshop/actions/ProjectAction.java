package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;
import org.apache.xmlbeans.XmlException;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.commons.util.StringUtils;
import soya.framework.xmlbeans.XmlSchemaTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class ProjectAction extends WorkshopAction<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            required = true,
            displayOrder = 1
    )
    protected String project;

    protected File getProjectDir() {
        return new File(workspace.getProjectHome(), project);
    }

    protected Project getProject() throws IOException {
        return workspace.getProject(project);
    }

    protected XmlSchemaTree schemaTree() throws XmlException, IOException {
        File file = new File(workspace.getCmmHome(), getProject().getSchemaFile());
        return new XmlSchemaTree(file);
    }

    protected Map<String, String> parse(File file) throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(file.toURI()))) {
            stream.forEach(e -> {
                String ln = e.trim();
                int index = ln.indexOf('=');
                if (index > 0) {
                    String key = ln.substring(0, index);
                    String value = ln.substring(index + 1).trim();
                    if (!value.isEmpty()) {
                        map.put(key, value);
                    }
                }
            });
        }

        return map;
    }

    static class Node {
        private Map<String, FunctionalExpression> functions = new LinkedHashMap<>();

        public boolean contains(String func) {
            return functions.containsKey(func);
        }

        public String[] getFunctionParameters(String func) {
            if (contains(func)) {
                return functions.get(func).getParameters();
            }

            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            functions.values().forEach(e -> {
                builder.append(e).append("::");
            });

            String st = builder.toString();
            if(st.endsWith("::")) {
                st = st.substring(0, st.length() - 2);
            }

            return st;
        }

        public static Node fromString(String expression) {
            Node node = new Node();
            String[] array = expression.split("::");
            Arrays.stream(array).forEach(e -> {
                FunctionalExpression fe = FunctionalExpression.parse(e);
                node.functions.put(fe.getName(), fe);
            });

            return node;
        }
    }

    static class FunctionalExpression {
        private String name;
        private String[] parameters;

        public FunctionalExpression(String name, String[] parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public String[] getParameters() {
            return parameters;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(name).append("(");
            for (int i = 0; i < getParameters().length; i++) {
                if (i > 0) {
                    builder.append(",");
                }
                builder.append(parameters[i]);
            }
            builder.append(")");
            return builder.toString();
        }

        static FunctionalExpression parse(String exp) {
            String token = exp.trim();
            String name = token.substring(0, token.indexOf("("));
            String[] params = token.substring(token.indexOf("(") + 1, token.lastIndexOf(")")).split(",");
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
            }
            return new FunctionalExpression(name, params);
        }

    }
}
