package com.albertsons.workshop.actions;

import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.bean.GenericTree;
import soya.framework.bean.TreeUtils;

import java.io.File;
import java.util.Map;

@ActionDefinition(domain = "workshop",
        name = "project-xpath-tree",
        path = "/project/xpath-tree",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class ProjectXPathTreeAction extends ProjectAction {

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            displayOrder = 2
    )
    private String file;

    @Override
    public String execute() throws Exception {
        File file = new File(getProjectDir(), "work/xpath-schema.properties");
        Map<String, String> map = parse(file);

        GenericTree<Node> tree = new GenericTree<>();
        map.entrySet().forEach(e -> {
            if (tree.root() == null) {
                tree.setRoot(e.getKey(), Node.fromString(e.getValue()));

            } else {
                String path = e.getKey();
                if (path.contains("/")) {
                    tree.add(path, Node.fromString(e.getValue()));
                }
            }
        });

        return TreeUtils.print(tree);
    }

}
