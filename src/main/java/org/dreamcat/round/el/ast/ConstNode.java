package org.dreamcat.round.el.ast;

import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;

/**
 * @author Jerry Will
 * @since 2021-07-09
 */
@RequiredArgsConstructor
public class ConstNode extends TreeNode {

    public final Object value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public ElNode copy() {
        return new ConstNode(value);
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        return value;
    }
}
