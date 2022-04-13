package org.dreamcat.round.el.function;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.round.el.exception.UnsupportedOpException;

/**
 * @author Jerry Will
 * @version 2021-07-25
 */
public enum InElFunction implements ElFunction {
    INSTANCE;

    static final String KEYWORD = "in";

    @Override
    public Object invoke(Object... args) {
        Object element = Objects.requireNonNull(args[0]);
        Object set = Objects.requireNonNull(args[1]);

        Class<?> clazz = set.getClass();
        if (set instanceof CharSequence && element instanceof CharSequence) {
            return set.toString().contains(element.toString());
        } else if (set instanceof Collection) {
            return ((Collection<?>) set).contains(element);
        } else if (clazz.isArray()) {
            if (!(set instanceof Object[])) {
                set = ReflectUtil.castArray(set);
            }
            for (Object e : ((Object[]) set)) {
                if (element.equals(e)) {
                    return true;
                }
            }
            return false;
        } else if (set instanceof Map) {
            return ((Map<?, ?>) set).containsKey(element);
        } else {
            throw new UnsupportedOpException(String.format("%s %s %s",
                    element.getClass(), KEYWORD, set.getClass()));
        }
    }
}
