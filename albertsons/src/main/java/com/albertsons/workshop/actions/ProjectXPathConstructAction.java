package com.albertsons.workshop.actions;

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
        name = "project-xpath-construct",
        path = "/project/xpath-construct",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ProjectXPathConstructAction extends ProjectAction {

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

        String mapping = mappingFile == null? "work/" + Workspace.MAPPING_FILE : mappingFile;
        String sheet = mappingSheet == null? Workspace.DEFAULT_MAPPING_SHEET : mappingSheet;

        File bod = getProjectDir();
        File xlsx = new File(bod, mapping);

        StringBuilder builder = new StringBuilder();
        XlsxDynaClass dynaClass = new XlsxDynaClass(xlsx.getName(), xlsx, sheet,
                Workspace.MAPPING_COLUMNS);

        Construct.annotate(getProject(), tree, dynaClass.getRows());

        tree.stream().forEach(e -> {
            builder.append(e.getPath()).append("=").append(getConstruction(e)).append("\n");
        });

        return builder.toString();
    }

    private String getConstruction(TreeNode<XsNode> node) {
        Construct construct = (Construct) node.getAnnotation("construct");
        return construct == null? "" : construct.toString();
    }
}
