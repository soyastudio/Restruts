package soya.framework.common.knowledge;

import java.util.List;

public interface TreeNode {

    TreeNode getParent();

    List<? extends TreeNode> getChildren();

    String getName();

    String getPath();

    Object getData();

}
