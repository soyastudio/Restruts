package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.xmlbeans.XmlSchemaTree;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

@ActionDefinition(domain = "workshop",
        name = "project-java-template",
        path = "/project/java-template",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ProjectJavaTemplateAction extends ProjectAction {
    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 2
    )
    private String mappingFile;

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 3
    )
    private String mappingSheet;

    @Override
    public String execute() throws Exception {
        XmlSchemaTree tree = schemaTree();
        File file = new File(getProjectDir(), "work/xpath-construct.properties");
        Map<String, String> rules = new LinkedHashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(file.toURI()))) {
            stream.forEach(e -> {
                String ln = e.trim();
                int index = ln.indexOf('=');
                if (index > 0) {
                    String key = ln.substring(0, index);
                    String value = ln.substring(index + 1).trim();
                    if (!value.isEmpty()) {
                        rules.put(key, value);
                    }
                }
            });
        }

        return new JsonCodeGenerator(getProject(), tree, rules).render();
    }
}
