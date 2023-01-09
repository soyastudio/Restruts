package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Workspace;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.poi.XlsxDynaClass;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;
import soya.framework.xmlbeans.XsUtils;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "project-mappings-validation",
        path = "/project/mappings-validation",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class ProjectMappingsValidateAction extends ProjectAction {

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
        XmlSchemaTree schemaTree = schemaTree();

        String mapping = mappingFile == null? "work/" + Workspace.MAPPING_FILE : mappingFile;
        String sheet = mappingSheet == null? Workspace.DEFAULT_MAPPING_SHEET : mappingSheet;

        File bod = getProjectDir();
        File xlsx = new File(bod, mapping);

        StringBuilder builder = new StringBuilder();
        XlsxDynaClass dynaClass = new XlsxDynaClass(xlsx.getName(), xlsx, sheet,
                Workspace.MAPPING_COLUMNS);

        dynaClass.getRows()
                .forEach(e -> {
                    if(e.get("Target") != null) {
                        String target = e.getAsString("Target");
                        String dataType = dataType(e.getAsString("DataType"));
                        String cardinality = e.getAsString("Cardinality");
                        String rule = e.getAsString("Mapping");
                        String source = e.getAsString("Source");

                        if(!isEmpty(target) && !isEmpty(rule) && !isEmpty(source)) {
                            if(schemaTree.contains(target)) {
                                XsNode xsNode = (XsNode) schemaTree.get(target).getData();
                                if(!XsUtils.type(xsNode).equalsIgnoreCase(dataType) || !XsUtils.cardinality(xsNode).equals(cardinality)) {
                                    builder.append(target)
                                            .append("=")
                                            .append("type(").append(dataType).append(")")
                                            .append("::")
                                            .append("cardinality(").append(cardinality).append(")")
                                            .append("\n");
                                }
                            }
                        }

                    }
                });
        return builder.toString();
    }

    private String dataType(String s) {
        if(s == null) {
            return null;
        }
        String token = s.trim();
        if(token.contains(" ")) {
            token = token.substring(0, token.indexOf(" "));
        }
        return token;
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }
}

