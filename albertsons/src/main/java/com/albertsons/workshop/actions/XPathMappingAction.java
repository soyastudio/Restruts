package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.bean.DynaBean;
import soya.framework.poi.XlsxDynaClass;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "xpath-mapping",
        path = "/xpath/mapping",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class XPathMappingAction extends WorkshopAction<String> {

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
        File bod = new File(workspace.getProjectHome(), projectName);
        File xlsx = new File(bod, fileName);

        StringBuilder builder = new StringBuilder();
        XlsxDynaClass dynaClass = new XlsxDynaClass(xlsx.getName(), xlsx, sheetName,
                new String[]{
                        "Target", "DataType", "Cardinality", "Mapping", "Source", "Version"
                });

        dynaClass.getRows().forEach(e -> {
            builder.append(e.get("Target"))
                    .append("=")
                    .append(getDataType(e))
                    .append("::")
                    .append(getCardinality(e))
                    .append("\n");
        });

        return builder.toString();
    }

    private String getDataType(DynaBean<?> bean) {
        String dataType = bean.get("DataType") == null ? "?" : bean.get("DataType").toString();
        return new StringBuilder("type(")
                .append(dataType)
                .append(")")
                .toString();
    }

    private String getCardinality(DynaBean<?> bean) {
        String cardinality = bean.get("Cardinality") == null ? "?" : bean.get("Cardinality").toString();
        return new StringBuilder("cardinality(")
                .append(cardinality)
                .append(")")
                .toString();
    }
}
