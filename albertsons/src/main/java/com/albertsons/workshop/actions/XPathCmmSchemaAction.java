package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.bean.TreeNode;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;
import soya.framework.xmlbeans.XsUtils;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "xpath-cmm-schema",
        path = "/xpath/cmm-schema",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class XPathCmmSchemaAction extends WorkshopAction<String> {

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM)
    private String schema;

    @Override
    public String execute() throws Exception {
        File file = workspace.findCmmFile(schema);

        XmlSchemaTree tree = new XmlSchemaTree(file);
        CodeBuilder codeBuilder = CodeBuilder.newInstance();
        render(tree.root(), codeBuilder);

        return codeBuilder.toString();
    }


    private void render(TreeNode<XsNode> node, CodeBuilder codeBuilder) {
        codeBuilder.append(node.getPath())
                .append("=").append("type(").append(XsUtils.type(node.getData())).append(")")
                .append("::").append("cardinality(").append(XsUtils.cardinality(node.getData())).appendLine(")");
        node.getChildren().forEach(e -> {
            render(e, codeBuilder);
        });
    }
}
