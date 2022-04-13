package org.dreamcat.round.el.function;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.round.el.exception.ExecuteException;
import org.dreamcat.round.el.exception.UnsupportedOpException;

/**
 * @author Jerry Will
 * @version 2021-08-01
 */
public enum DotElFunction implements ElFunction {
    INSTANCE;

    @Override
    public Object invoke(Object... args) {
        Object object = Objects.requireNonNull(args[0]);
        String property = Objects.requireNonNull((String) args[1]);

        if (object instanceof Map) {
            return ((Map<?, ?>) object).get(property);
        } else if (object instanceof Class) {
            try {
                Field field = ReflectUtil.retrieveField((Class<?>) object, property);
                field.setAccessible(true);
                return field.get(null);
            } catch (NoSuchFieldException e) {
                throw new UnsupportedOpException(String.format(
                        "unknown property %s in type %s", property, object.getClass()));
            } catch (IllegalAccessException e) {
                throw new ExecuteException(e);
            }
        }

        try {
            return ReflectUtil.getFieldValue(object, property);
        } catch (NoSuchFieldException e) {
            throw new UnsupportedOpException(String.format(
                    "unknown property %s in type %s", property, object.getClass()));
        }
    }
}
