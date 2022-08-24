package soya.framework.restruts.actions.xmlbeans;

import org.apache.xmlbeans.SchemaTypeSystem;
import soya.framework.commons.knowledge.KnowledgeTree;
import soya.framework.commons.knowledge.KnowledgeTreeNode;
import soya.framework.commons.util.CodeBuilder;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;
import soya.framework.restruts.actions.xmlbeans.xs.XsNode;
import soya.framework.restruts.actions.xmlbeans.xs.XsUtils;

@OperationMapping(domain = "document",
        name = "xmlbeans-xpath-schema",
        path = "/xmlbean/xpath-schema",
        method = OperationMapping.HttpMethod.POST,
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
