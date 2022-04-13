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
public class WhileNode extends ElNode {

    public ElNode cond;
    public ElNode body;

    @Override
    public ElNode copy() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("while( ");
        s.append(cond);
        s.append(" ) {\n");
        if (body != null) s.append(body);
        s.append("\n}");
        return s.toString();
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        while (Objects.equals(cond.evaluate(context, engine), true)) {
            try {
                body.evaluate(context, engine);
            } catch (ContinueException ignore) {
                // nop
            } catch (BreakException e) {
                break;
            }
        }
        return null;
    }
}
