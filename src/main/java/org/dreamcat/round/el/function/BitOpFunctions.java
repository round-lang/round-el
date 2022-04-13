package org.dreamcat.round.el.function;

import java.util.Objects;
import org.dreamcat.round.el.exception.UnsupportedOpException;

/**
 * @author Jerry Will
 * @version 2022-04-11
 */
final class BitOpFunctions {

    private BitOpFunctions() {
    }

    static final ElFunction BIT_AND_OP = args -> {
        Object a = Objects.requireNonNull(args[0]);
        Object b = Objects.requireNonNull(args[1]);
        if (a instanceof Number && b instanceof Number) {
            if ((a instanceof Integer || a instanceof Short || a instanceof Byte) &&
                    b instanceof Integer || b instanceof Short || b instanceof Byte) {
                return ((Number) a).intValue() & ((Number) b).intValue();
            }
            return ((Number) a).longValue() & ((Number) b).longValue();
        }
        throw new UnsupportedOpException(
                String.format("%s & %s", a.getClass(), b.getClass()));
    };

    static final ElFunction BIT_OR_OP = args -> {
        Object a = Objects.requireNonNull(args[0]);
        Object b = Objects.requireNonNull(args[1]);
        if (a instanceof Number && b instanceof Number) {
            if ((a instanceof Integer || a instanceof Short || a instanceof Byte) &&
                    b instanceof Integer || b instanceof Short || b instanceof Byte) {
                return ((Number) a).intValue() | ((Number) b).intValue();
            }
            return ((Number) a).longValue() | ((Number) b).longValue();
        }
        throw new UnsupportedOpException(
                String.format("%s | %s", a.getClass(), b.getClass()));
    };

    static final ElFunction BIT_XOR_OP = args -> {
        Object a = Objects.requireNonNull(args[0]);
        Object b = Objects.requireNonNull(args[1]);
        if (a instanceof Number && b instanceof Number) {
            if ((a instanceof Integer || a instanceof Short || a instanceof Byte) &&
                    b instanceof Integer || b instanceof Short || b instanceof Byte) {
                return ((Number) a).intValue() ^ ((Number) b).intValue();
            }
            return ((Number) a).longValue() ^ ((Number) b).longValue();
        }
        throw new UnsupportedOpException(
                String.format("%s ^ %s", a.getClass(), b.getClass()));
    };

    static final ElFunction BIT_NOT_OP = args -> {
        Object a = Objects.requireNonNull(args[0]);
        if (a instanceof Integer) {
            return ~((int) a);
        } else if (a instanceof Number) {
            return ~((long) a);
        }
        throw new UnsupportedOpException(String.format("~ %s", a.getClass()));
    };

}
