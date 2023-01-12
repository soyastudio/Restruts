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
        name = "project-esql-template",
        path = "/project/esql-template",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ProjectESQLTemplateAction extends ProjectAction {
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

        String mapping = mappingFile == null ? "work/" + Workspace.MAPPING_FILE : mappingFile;
        String sheet = mappingSheet == null ? Workspace.DEFAULT_MAPPING_SHEET : mappingSheet;

        File bod = getProjectDir();
        File xlsx = new File(bod, mapping);

        XlsxDynaClass dynaClass = new XlsxDynaClass(xlsx.getName(), xlsx, sheet,
                Workspace.MAPPING_COLUMNS);

        Construct.annotate(getProject(), tree, dynaClass.getRows());

        return Construct.generateESQLTemplate(tree);
    }
}
