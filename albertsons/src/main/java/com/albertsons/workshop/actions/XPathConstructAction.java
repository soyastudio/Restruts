package com.albertsons.workshop.actions;

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
        name = "xpath-construct",
        path = "/xpath/construct",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class XPathConstructAction extends WorkshopAction<String> {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
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
                    if (e.get("Target") != null && e.get("Source") != null) {
                        String target = e.getAsString("Target");
                        if (tree.contains(target)) {
                            annotate(tree.get(target), e.getAsString("Mapping"), e.get("Source").toString());
                        }
                    }
                });

        tree.stream().forEach(e -> {
            builder.append(e.getPath()).append("=").append(getConstruction(e)).append("\n");
        });

        return builder.toString();
    }

    private void annotate(TreeNode<XsNode> node, String mapping, String source) {
        node.annotate("construct", Construct.createAssignment(node, mapping, source));
        TreeNode<XsNode> parent = node.getParent();
        while (parent != null && parent.getAnnotation("construct") == null) {
            parent.annotate("construct", Construct.createComplexConstruct(parent));
            parent = parent.getParent();
        }
    }

    private String getConstruction(TreeNode<XsNode> node) {
        Construct construct = (Construct) node.getAnnotation("construct");
        return construct == null? "" : construct.toString();
    }

}
