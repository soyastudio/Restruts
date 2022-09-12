package soya.framework.document.actions.xmlbeans;

import org.apache.xmlbeans.SchemaTypeSystem;
import soya.framework.common.knowledge.KnowledgeTree;
import soya.framework.document.actions.xmlbeans.xs.XsKnowledgeSystem;
import soya.framework.document.actions.xmlbeans.xs.XsNode;
import soya.framework.document.actions.DocumentAction;

public abstract class XmlBeansAction<T> extends DocumentAction<T> {

    protected KnowledgeTree<SchemaTypeSystem, XsNode> extract(String uri) throws Exception {
        return XsKnowledgeSystem.knowledgeTree(getFile(uri));
    }

}
