package org.dreamcat.round.el.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.dreamcat.common.Pair;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.common.util.ObjectUtil;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.round.el.exception.ExecuteException;
import org.dreamcat.round.el.exception.UnsupportedOpException;

/**
 * a[1]
 * a[1..-1]
 * a[1,2,3]
 * a[1,2..-2,3..6]
 *
 * @author Jerry Will
 * @version 2021-08-24
 */
@SuppressWarnings({"unchecked"})
public enum BracketFunction implements ElFunction {
    INSTANCE;

    @Override
    public Object invoke(Object... args) {
        Object object = Objects.requireNonNull(args[0]);
        Object arg1 = Objects.requireNonNull(args[1]);
        List<?> index = castAsList(arg1);
        if (ObjectUtil.isEmpty(index)) {
            throw new ExecuteException("empty index on [] op");
        }

        Object result = object;
        for (Object ind : index) {
            Class<?> clazz = result.getClass();
            if (ind instanceof Pair) {
                Pair<Number, Number> p = (Pair<Number, Number>) ind;
                int a = p.first().intValue(), b = p.second().intValue();
                if (clazz.isArray()) {
                    int size = ArrayUtil.length(result);
                    if (a < 0) a = size + a;
                    if (b < 0) b = size + b;
                    result = ArrayUtil.copyOfRange(result, a, b);
                } else if (result instanceof List) {
                    List<?> list = (List<?>) result;
                    int size = list.size();
                    if (a < 0) a = size + a;
                    if (b < 0) b = size + b;
                    result = new ArrayList<>(list.subList(a, b));
                } else throwOp(clazz, index);
            } else if (ind instanceof Number) {
                int a = ((Number) ind).intValue();
                if (clazz.isArray()) {
                    if (a < 0) a = ArrayUtil.length(result) + a;
                    result = ArrayUtil.get(result, a);
                } else if (result instanceof List) {
                    List<?> list = (List<?>) result;
                    if (a < 0) a = list.size() + a;
                    result = list.get(a);
                } else throwOp(clazz, index);
            } else {
                throwOp(clazz, index);
            }
        }
        return result;
    }

    private List<?> castAsList(Object o) {
        if (o.getClass().isArray()) {
            Collection<?> c = ReflectUtil.castAsCollection(o);
            if (c instanceof List) return (List<?>) c;
            else return new ArrayList<>(c);
        }
        return (List<?>) o;
    }

    private void throwOp(Class<?> clazz, List<?> index) {
        throw new UnsupportedOpException(String.format(
                "unsupported [] op for %s, index %s", clazz, index));
    }
}
