package org.dreamcat.round.el.function;

import static org.dreamcat.round.el.function.ArithmeticOpFunctions.newOp;

import org.dreamcat.common.util.NumberUtil;
import org.dreamcat.common.util.ObjectUtil;

/**
 * @author Jerry Will
 * @version 2021-08-01
 */
final class CompareOpFunctions {

    private CompareOpFunctions() {
    }

    static final ElFunction LT_OP = newOp("<", NumberUtil::lt);
    static final ElFunction LE_OP = newOp("<=", NumberUtil::le);
    static final ElFunction GT_OP = newOp(">", NumberUtil::gt);
    static final ElFunction GE_OP = newOp(">=", NumberUtil::ge);

    static final ElFunction EQ_OP = arguments -> {
        Object a = arguments[0];
        Object b = arguments[1];

        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.equals(b)) return true;

        if (a instanceof Number && b instanceof Number) {
            return NumberUtil.eq((Number) a, (Number) b);
        } else {
            return ObjectUtil.deepEquals(a, b);
        }
    };

    static final ElFunction NE_OP = arguments ->
            !(boolean) (EQ_OP.invoke(arguments));

}
