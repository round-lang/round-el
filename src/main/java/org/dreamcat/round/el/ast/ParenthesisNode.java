package org.dreamcat.round.el.ast;

import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;

/**
 * @author Jerry Will
 * @version 2021-08-16
 */
public class ParenthesisNode extends TreeNode {

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("( ");
        int size = children.size();
        if (size >= 1) {
            s.append(children.get(0));
            for (int i = 1; i < size; i++) {
                s.append(", ").append(children.get(i));
            }
        }
        s.append(" )");
        return s.toString();
    }

    @Override
    public ElNode copy() {
        ParenthesisNode node = new ParenthesisNode();
        for (ElNode child : children) {
            node.addChild(child.copy());
        }
        return node;
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        return children.get(0).evaluate(context, engine);
    }
}
