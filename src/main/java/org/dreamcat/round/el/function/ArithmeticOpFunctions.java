package org.dreamcat.round.el.function;

import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import org.dreamcat.common.util.NumberUtil;
import org.dreamcat.round.el.exception.UnsupportedOpException;

/**
 * @author Jerry Will
 * @version 2021-08-01
 */
public class ArithmeticOpFunctions {

    private ArithmeticOpFunctions() {
    }

    private static final ElFunction POS_OP = arguments -> {
        Object a = Objects.requireNonNull(arguments[0]);
        if (a instanceof Number) {
            return a;
        }
        throw new UnsupportedOpException(" + " + a.getClass());
    };
    private static final ElFunction NEG_OP = arguments -> {
        Object a = Objects.requireNonNull(arguments[0]);
        if (a instanceof Number) {
            return NumberUtil.neg((Number) a);
        }
        throw new UnsupportedOpException(" - " + a.getClass());
    };

    public static final ElFunction ADD_OP = newOp("+", NumberUtil::add, POS_OP);
    public static final ElFunction SUB_OP = newOp("-", NumberUtil::subtract, NEG_OP);
    public static final ElFunction MUL_OP = newOp("*", NumberUtil::multiply);
    public static final ElFunction DOUBLE_MUL_OP = newOp("**", NumberUtil::pow);
    public static final ElFunction REM_OP = newOp("%", NumberUtil::remainder);

    static ElFunction newOp(String keyword, BiFunction<Number, Number, Object> op) {
        return args -> {
            Object a = Objects.requireNonNull(args[0]);
            Object b = Objects.requireNonNull(args[1]);

            if (a instanceof Number && b instanceof Number) {
                Object r = op.apply((Number) a, (Number) b);
                if (r != null) return r;
            }
            throw new UnsupportedOpException(
                    String.format("%s %s %s", a.getClass(), keyword, b.getClass()));
        };
    }

    static ElFunction newOp(String keyword, BinaryOperator<Number> op, ElFunction unaryOp) {
        return arguments -> {
            Object a = Objects.requireNonNull(arguments[0]);
            if (arguments.length == 1) return unaryOp.invoke(a);
            Object b = Objects.requireNonNull(arguments[1]);

            if (a instanceof Number && b instanceof Number) {
                Object r = op.apply((Number) a, (Number) b);
                if (r != null) return r;
            }
            throw new UnsupportedOpException(
                    String.format("%s %s %s", a.getClass(), keyword, b.getClass()));
        };
    }

    static final ElFunction DIV_OP = newNumberDivOp(RoundingMode.HALF_EVEN);

    public static ElFunction newNumberDivOp(RoundingMode roundingMode) {
        return arguments -> {
            Object a = Objects.requireNonNull(arguments[0]);
            Object b = Objects.requireNonNull(arguments[1]);

            if (a instanceof Number && b instanceof Number) {
                Object r = NumberUtil.divide((Number) a, (Number) b, roundingMode);
                if (r != null) return r;
            }
            throw new UnsupportedOpException(
                    String.format("%s / %s", a.getClass(), b.getClass()));
        };
    }

}
