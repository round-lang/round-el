package org.dreamcat.round.el.ast;

import java.util.Objects;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;

/**
 * if ( ) { } else { }
 *
 * @author Jerry Will
 * @version 2021-08-14
 */
public class IfNode extends ElNode {

    public ElNode cond;
    public ElNode thenPart;
    public ElNode elsePart;

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("if (");
        s.append(cond);
        s.append(" ) { ");
        s.append(thenPart);
        s.append(" }");
        if (elsePart != null) {
            s.append(" else { ");
            s.append(elsePart);
            s.append(" }");
        }
        return s.toString();
    }

    @Override
    public ElNode copy() {
        IfNode node = new IfNode();
        node.cond = cond.copy();
        node.thenPart = thenPart.copy();
        if (elsePart != null) node.elsePart = elsePart.copy();
        return node;
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        if (Objects.equals(cond.evaluate(context, engine), true)) {
            return thenPart.evaluate(context, engine);
        } else if (elsePart != null) {
            return elsePart.evaluate(context, engine);
        } else return null;
    }
}
