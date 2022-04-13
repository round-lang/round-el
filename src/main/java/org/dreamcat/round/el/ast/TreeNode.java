package org.dreamcat.round.el.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
public abstract class TreeNode extends ElNode {

    public List<ElNode> children = new ArrayList<>();

    @Override
    public void addChild(ElNode child) {
        children.add(child);
        child.parent = this;
    }

    @Override
    public void removeChild(ElNode child) {
        children.remove(child);
    }
}
