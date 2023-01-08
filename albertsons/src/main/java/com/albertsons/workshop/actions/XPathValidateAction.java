package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.poi.XlsxDynaClass;
import soya.framework.xmlbeans.XmlSchemaTree;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "xpath-validation",
        path = "/xpath/validation",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class XPathValidateAction extends WorkshopAction<String> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 0
    )
    private String schemaName;

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 1
    )
    private String projectName;

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 2
    )
    private String fileName;

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 3
    )
    private String sheetName = "Mappings";

    @Override
    public String execute() throws Exception {
        File file = workspace.findCmmFile(schemaName);
        XmlSchemaTree tree = new XmlSchemaTree(file);

        File bod = new File(workspace.getProjectHome(), projectName);
        File xlsx = new File(bod, fileName);

        StringBuilder builder = new StringBuilder();
        new XlsxDynaClass(xlsx.getName(), xlsx, sheetName,
                new String[]{
                        "Target", "DataType", "Cardinality", "Mapping", "Source", "Version"
                })
                .getRows()
                .forEach(e -> {
                    if(e.get("Target") != null) {
                        String target = (String) e.get("Target");
                        if(target.trim().length() > 0 && !tree.contains(target)) {
                            builder.append(target).append("\n");
                        }
                    }
                });
        return builder.toString();
    }
}
