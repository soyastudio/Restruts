package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.bean.TreeNode;
import soya.framework.bean.TreeUtils;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.xmlbeans.XmlSchemaTree;
import soya.framework.xmlbeans.XsNode;
import soya.framework.xmlbeans.XsUtils;

@ActionDefinition(domain = "workshop",
        name = "project-cmm-tree",
        path = "/project/cmm-tree",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class ProjectCmmTreeAction extends ProjectAction{

    @Override
    public String execute() throws Exception {
        XmlSchemaTree tree = schemaTree();
        return TreeUtils.print(tree);
    }
}
