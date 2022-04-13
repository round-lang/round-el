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
 * @author Jerry Will
 * @version 2021-08-24
 */
@SuppressWarnings({"unchecked"})
public enum BracketFunction implements ElFunction {
    INSTANCE;

    @Override
    public Object invoke(Object... args) {
        Object object = Objects.requireNonNull(args[0]);
        // only [1], [1..2] is supported
        Object arg1 = Objects.requireNonNull(args[1]);
        List<?> index = castAsList(arg1);
        if (ObjectUtil.isEmpty(index)) {
            throw new ExecuteException("empty index on [] op");
        }
        Object ind = index.get(0);

        Class<?> clazz = object.getClass();
        if (ind instanceof Pair) {
            Pair<Number, Number> p = (Pair<Number, Number>) ind;
            int a = p.first().intValue(), b = p.second().intValue();

            if (clazz.isArray()) {
                int size = ArrayUtil.length(object);
                if (a < 0) a = size + a;
                if (b < 0) b = size + b;
                return ArrayUtil.copyOfRange(object, a, b);
            } else if (object instanceof List) {
                List<?> list = (List<?>) object;
                int size = list.size();
                if (a < 0) a = size + a;
                if (b < 0) b = size + b;
                return new ArrayList<>(list.subList(a, b));
            }
        } else {
            int a = ((Number) ind).intValue();
            if (clazz.isArray()) {
                if (a < 0) a = ArrayUtil.length(object) + a;
                return ArrayUtil.get(object, a);
            } else if (object instanceof List) {
                List<?> list = (List<?>) object;
                if (a < 0) a = list.size() + a;
                return list.get(a);
            }
        }
        throw new UnsupportedOpException(String.format(
                "unsupported [] op between %s and %s", clazz, index.getClass()));
    }

    private List<?> castAsList(Object o) {
        if (o.getClass().isArray()) {
            Collection<?> c = ReflectUtil.castAsCollection(o);
            if (c instanceof List) return (List<?>) c;
            else return new ArrayList<>(c);
        }
        return (List<?>) o;
    }
}
