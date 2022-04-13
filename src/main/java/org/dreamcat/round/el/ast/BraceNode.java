package org.dreamcat.round.el.ast;

import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.exception.ReturnException;

/**
 * @author Jerry Will
 * @version 2021-08-16
 */
public class BraceNode extends TreeNode {

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("{ ");
        int size = children.size();
        if (size >= 1) {
            s.append(children.get(0));
            for (int i = 1; i < size; i++) {
                s.append(", ").append(children.get(i));
            }
        }
        s.append(" }");
        return s.toString();
    }

    @Override
    public ElNode copy() {
        BraceNode node = new BraceNode();
        for (ElNode child : children) {
            node.addChild(child.copy());
        }
        return node;
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        Object result = null;
        if (parent != null) {
            for (ElNode child : children) {
                result = child.evaluate(context, engine);
            }
        } else {
            for (ElNode child : children) {
                try {
                    result = child.evaluate(context, engine);
                } catch (ReturnException e) {
                    return e.getValue();
                }
            }
        }
        return result;
    }
}
