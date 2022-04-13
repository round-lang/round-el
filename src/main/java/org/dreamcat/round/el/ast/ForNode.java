package org.dreamcat.round.el.ast;

import java.util.Objects;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.exception.BreakException;
import org.dreamcat.round.el.exception.ContinueException;

/**
 * @author Jerry Will
 * @version 2021-08-23
 */
public class ForNode extends ElNode {

    public ElNode pre;
    public ElNode cond;
    public ElNode post;
    public ElNode body;

    @Override
    public ElNode copy() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("for( ");
        if (pre != null) s.append(pre);
        s.append("; ");
        if (cond != null) s.append(cond);
        s.append("; ");
        if (post != null) s.append(post);
        s.append(" ) {\n");
        if (body != null) s.append(body);
        s.append("\n}");
        return s.toString();
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        if (pre != null) {
            pre.evaluate(context, engine);
        }
        while (cond == null || Objects.equals(cond.evaluate(context, engine), true)) {
            try {
                body.evaluate(context, engine);
            } catch (ContinueException ignore) {
                // nop
            } catch (BreakException e) {
                break;
            }
            if (post != null) post.evaluate(context, engine);
        }
        return null;
    }
}
