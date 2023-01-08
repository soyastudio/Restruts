package soya.framework.xmlbeans;

import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;
import soya.framework.bean.TreeNode;
import soya.framework.commons.util.CodeBuilder;

import java.io.File;

@ActionDefinition(domain = "xmlbeans",
        name = "xmlbeans-xpath-schema",
        path = "/xpath-schema",
        method = ActionDefinition.HttpMethod.GET,
        produces = MediaType.TEXT_PLAIN)
public class XPathSchemaAction extends XmlBeansAction<String> {

    @Override
    public String execute() throws Exception {
        File file = getFile(schemaURI);

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
