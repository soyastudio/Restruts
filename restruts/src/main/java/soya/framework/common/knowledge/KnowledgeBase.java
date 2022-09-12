package soya.framework.common.knowledge;

public interface KnowledgeBase<K extends KnowledgeNode> {
    Object getSource();

    K getKnowledge();
}
