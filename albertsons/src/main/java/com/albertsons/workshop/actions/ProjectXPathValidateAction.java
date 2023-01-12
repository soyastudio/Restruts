package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Workspace;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.poi.XlsxDynaClass;
import soya.framework.xmlbeans.XmlSchemaTree;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "project-xpath-validation",
        path = "/project/xpath-validation",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class ProjectXPathValidateAction extends ProjectAction {

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
                        if(!schemaTree.contains(target)) {
                            builder.append(target).append("\n");
                        }

                    }
                });
        return builder.toString();
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }
}
