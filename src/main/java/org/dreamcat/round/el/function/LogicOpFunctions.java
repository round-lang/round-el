package org.dreamcat.round.el.function;

import java.util.Objects;
import org.dreamcat.round.el.exception.UnsupportedOpException;

/**
 * @author Jerry Will
 * @version 2021-08-01
 */
final class LogicOpFunctions {

    private LogicOpFunctions() {
    }

    static final ElFunction AND_OP = args -> {
        Object a = Objects.requireNonNull(args[0]);
        Object b = Objects.requireNonNull(args[1]);
        if (a instanceof Boolean && b instanceof Boolean) {
            return (boolean) a && (boolean) b;
        }
        throw new UnsupportedOpException(
                String.format("%s && %s", a.getClass(), b.getClass()));
    };

    static final ElFunction OR_OP = args -> {
        Object a = Objects.requireNonNull(args[0]);
        Object b = Objects.requireNonNull(args[1]);
        if (a instanceof Boolean && b instanceof Boolean) {
            return (boolean) a || (boolean) b;
        }
        throw new UnsupportedOpException(
                String.format("%s && %s", a.getClass(), b.getClass()));
    };

    static final ElFunction NOT_OP = args -> {
        Object a = Objects.requireNonNull(args[0]);
        if (a instanceof Boolean) {
            return !(boolean) a;
        }
        throw new UnsupportedOpException(
                String.format("! %s", a.getClass()));
    };
}
