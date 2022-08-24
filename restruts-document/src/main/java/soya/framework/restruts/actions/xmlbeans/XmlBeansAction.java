package soya.framework.restruts.actions.xmlbeans;

import org.apache.xmlbeans.SchemaTypeSystem;
import soya.framework.commons.knowledge.KnowledgeTree;
import soya.framework.restruts.actions.DocumentAction;
import soya.framework.restruts.actions.xmlbeans.xs.XsKnowledgeSystem;
import soya.framework.restruts.actions.xmlbeans.xs.XsNode;

public abstract class XmlBeansAction<T> extends DocumentAction<T> {

    protected KnowledgeTree<SchemaTypeSystem, XsNode> extract(String uri) throws Exception {
        return XsKnowledgeSystem.knowledgeTree(getFile(uri));
    }

}
