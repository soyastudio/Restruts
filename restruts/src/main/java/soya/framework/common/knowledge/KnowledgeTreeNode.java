package soya.framework.common.knowledge;

import java.util.List;

public interface KnowledgeTreeNode<T> extends TreeNode, KnowledgeNode<T> {

    KnowledgeTreeNode getParent();

    List<KnowledgeTreeNode> getChildren();

    KnowledgeNode<T> getData();

}
