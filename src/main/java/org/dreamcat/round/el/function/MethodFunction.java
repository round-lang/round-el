package org.dreamcat.round.el.function;

import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.util.ReflectUtil;
import org.dreamcat.round.el.exception.ExecuteException;

/**
 * @author Jerry Will
 * @version 2021-08-01
 */
@RequiredArgsConstructor
public class MethodFunction implements ElFunction {

    final Method method;

    @Override
    public Object invoke(Object... args) {
        Object object = args[0];
        int size = args.length;
        try {
            Class<?> clazz = method.getDeclaringClass();
            if (size == 1) {
                return ReflectUtil.invoke(object, clazz, method);
            } else {
                return ReflectUtil.invoke(object, clazz, method,
                        Arrays.copyOfRange(args, 1, size));
            }
        } catch (Exception e) {
            throw new ExecuteException(e);
        }
    }

    public MethodFunction(Object object, String methodName, Class<?>... parameterTypes) {
        Method m;
        Class<?> clazz;
        if (!(object instanceof Class)) {
            clazz = object.getClass();
        } else clazz = (Class<?>) object;
        try {
            // try to find by name, since the primitive types not equals to the box types
            m = ReflectUtil.retrieveMethod(clazz, methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ExecuteException(e);
        }
        this.method = m;
    }
}
