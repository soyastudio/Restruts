package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.bean.TreeNode;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;
import soya.framework.xmlbeans.XsUtils;

import java.io.File;

@ActionDefinition(domain = "workshop",
        name = "project-cmm",
        path = "/project/cmm",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ProjectCmmAction extends ProjectAction{

    @Override
    public String execute() throws Exception {
        XmlSchemaTree tree = schemaTree();
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
