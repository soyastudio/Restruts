package com.albertsons.workshop.actions;

import com.albertsons.workshop.configuration.Project;
import com.albertsons.workshop.configuration.Workspace;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.bean.TreeNode;
import soya.framework.poi.XlsxDynaClass;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "project-esql-construct",
        path = "/project/esql-construct",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class ProjectESQLGenAction extends ProjectAction {
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

        Project project = getProject();
        File bod = getProjectDir();
        File xlsx = new File(bod, mapping);

        XlsxDynaClass dynaClass = new XlsxDynaClass(xlsx.getName(), xlsx, sheet,
                Workspace.MAPPING_COLUMNS);

        Construct.annotate(getProject(), tree, dynaClass.getRows());

        return Construct.generateESQLTemplate(tree, project);
    }
}
