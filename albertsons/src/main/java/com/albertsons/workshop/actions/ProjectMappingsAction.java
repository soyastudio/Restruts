package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Workspace;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.bean.DynaBean;
import soya.framework.poi.XlsxDynaClass;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "project-mappings",
        path = "/project/mappings",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ProjectMappingsAction extends ProjectAction {

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
        String mapping = mappingFile == null? "work/" + Workspace.MAPPING_FILE : mappingFile;
        String sheet = mappingSheet == null? Workspace.DEFAULT_MAPPING_SHEET : mappingSheet;

        File bod = getProjectDir();
        File xlsx = new File(bod, mapping);

        StringBuilder builder = new StringBuilder();
        XlsxDynaClass dynaClass = new XlsxDynaClass(xlsx.getName(), xlsx, sheet,
                Workspace.MAPPING_COLUMNS);

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
