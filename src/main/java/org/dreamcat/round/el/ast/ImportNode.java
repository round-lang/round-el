package org.dreamcat.round.el.ast;

import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.exception.ExecuteException;

/**
 * import a.b.c;
 * import a.b.c as x;
 * import a.b.*;
 * import {c, d as e} from a.b;
 *
 * @author Jerry Will
 * @version 2021-08-15
 */
@RequiredArgsConstructor
public class ImportNode extends ElNode {

    public final Class<?> clazz;
    public String as;

    public static ImportNode valueOf(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ExecuteException(e);
        }
        return new ImportNode(clazz);
    }

    @Override
    public String toString() {
        return "import " + clazz.getName();
    }

    @Override
    public ElNode copy() {
        return new ImportNode(clazz);
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        context.set(clazz.getSimpleName(), clazz);
        return null;
    }
}
