package org.dreamcat.round.el.function;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.exception.ExecuteException;

/**
 * @author Jerry Will
 * @version 2021-08-23
 */
@RequiredArgsConstructor
public class ConstructorFunction implements ElFunction {

    final Constructor<?> constructor;

    @Override
    public Object invoke(Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ExecuteException(e);
        }
    }

    public ConstructorFunction(Class<?> clazz, Class<?>... parameterTypes) {
        try {
            this.constructor = clazz.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ExecuteException(e);
        }
    }
}
