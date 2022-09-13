package soya.framework.document.actions.xmlbeans;

import org.apache.xmlbeans.SchemaTypeSystem;
import soya.framework.common.knowledge.KnowledgeTree;
import soya.framework.common.knowledge.KnowledgeTreeNode;
import soya.framework.common.util.CodeBuilder;
import soya.framework.document.actions.xmlbeans.xs.XsNode;
import soya.framework.document.actions.xmlbeans.xs.XsUtils;
import soya.framework.action.MediaType;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ParameterMapping;

@ActionDefinition(domain = "document",
        name = "xmlbeans-xpath-schema",
        path = "/xmlbean/xpath-schema",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class XPathSchemaAction extends XmlBeansAction<String> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    protected String uri;

    @Override
    public String execute() throws Exception {
        KnowledgeTree<SchemaTypeSystem, XsNode> tree = extract(uri);

        CodeBuilder codeBuilder = CodeBuilder.newInstance();
        render(tree.root(), codeBuilder);

        return codeBuilder.toString();
    }

    private void render(KnowledgeTreeNode<XsNode> node, CodeBuilder codeBuilder) {
        codeBuilder.append(node.getPath())
                .append("=").append("type(").append(XsUtils.type(node.origin())).append(")")
                .append("::").append("cardinality(").append(XsUtils.cardinality(node.origin())).appendLine(")");
        node.getChildren().forEach(e -> {
            render(e, codeBuilder);
        });
    }
}
