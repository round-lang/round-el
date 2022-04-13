package org.dreamcat.round.el.function;

import static org.dreamcat.round.el.function.ArithmeticOpFunctions.newOp;

import java.util.Collection;
import org.dreamcat.common.util.CollectionUtil;
import org.dreamcat.common.util.NumberUtil;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.round.el.exception.UnsupportedOpException;

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

        Class<?> c1 = a.getClass(), c2 = b.getClass();
        if (a instanceof Number && b instanceof Number) {
            return NumberUtil.eq((Number) a, (Number) b);
        } else if (ReflectUtil.isCollectionOrArray(c1) && ReflectUtil.isCollectionOrArray(c2)) {
            if (c1.isArray()) a = ReflectUtil.castAsCollection(a);
            if (c2.isArray()) b = ReflectUtil.castAsCollection(b);
            return CollectionUtil.deepEquals((Collection<?>) a, (Collection<?>) b);
        }
        throw new UnsupportedOpException(
                String.format("%s / %s", a.getClass(), b.getClass()));
    };

    static final ElFunction NE_OP = arguments ->
            !(boolean) (EQ_OP.invoke(arguments));

}
