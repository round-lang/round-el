package org.dreamcat.round.el;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import org.dreamcat.round.el.function.ElFunction;
import org.dreamcat.round.el.function.ElFunctions;
import org.dreamcat.round.el.function.MethodFunction;
import org.dreamcat.round.el.lex.OperatorToken;
import org.dreamcat.round.el.util.ElClassUtil;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@SuppressWarnings({"unchecked"})
class SimpleElEngine implements ElEngine {

    ElSettings settings = new ElSettings();

    final Map<String, Object> objs = new ConcurrentHashMap<>();
    final Map<OperatorToken, ElFunction> ops = new ConcurrentHashMap<>(ElFunctions.OPERATORS);
    final Map<String, ElFunction> fns = new ConcurrentHashMap<>(ElFunctions.FUNCTIONS);
    final Map<OperatorToken, Map<Class<?>, Map<Class<?>, ElFunction>>> regOpFns =
            new ConcurrentHashMap<>();
    final Map<Class<?>, Map<String, ElFunction>> extFns = new ConcurrentHashMap<>();
    final Map<Class<?>, Map<String, ElFunction>> methodFns = new ConcurrentHashMap<>();

    @Override
    public ElString compile(String expression) {
        return new SimpleElString(expression, this);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public ElSettings getSettings() {
        return settings;
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    @Override
    public Object getObject(String name) {
        Object value = objs.get(name);
        if (value == null) {
            return ElClassUtil.findBuiltinClass(name);
        }
        return value;
    }

    @Override
    public void setObject(String name, Object value) {
        objs.put(name, value);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    @Override
    public ElFunction getFunction(String name) {
        return fns.get(name);
    }

    @Override
    public ElFunction getFunction(OperatorToken operator) {
        return ops.get(operator);
    }

    @Override
    public void setFunction(OperatorToken operator, ElFunction function) {
        ops.put(operator, function);
    }

    @Override
    public void setFunction(String name, ElFunction function) {
        fns.put(name, function);
    }

    public void setFunction(String name, OperatorToken operator) {
        fns.put(name, getFunction(operator));
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    @Override
    public <T, R> ElFunction getRegisteredFunction(OperatorToken operator, Class<T> left, Class<R> right) {
        return regOpFns.getOrDefault(operator, Collections.emptyMap())
                .getOrDefault(left, Collections.emptyMap())
                .get(right);
    }

    @Override
    public ElFunction getExtendedFunction(Class<?> clazz, String methodName) {
        return extFns.getOrDefault(clazz, Collections.emptyMap())
                .get(methodName);
    }

    @Override
    public ElFunction getMethodFunction(Class<?> clazz, String methodName) {
        return methodFns.getOrDefault(clazz, Collections.emptyMap())
                .get(methodName);
    }

    @Override
    public <T, R> void setRegisterFunction(OperatorToken operator, Class<T> left, Class<R> right,
            BiFunction<T, R, Object> fn) {
        regOpFns.computeIfAbsent(operator, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(left, k -> new ConcurrentHashMap<>())
                .put(right, args -> fn.apply((T) args[0], (R) args[1]));
    }

    @Override
    public <T> void setExtendedFunction(Class<T> clazz, String name, BiFunction<T, Object[], Object> fn) {
        extFns.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>())
                .put(name, args -> {
                    if (args.length == 1) {
                        return fn.apply((T) args[0], new Object[0]);
                    } else {
                        return fn.apply((T) args[0], Arrays.copyOfRange(args, 1, args.length));
                    }
                });
    }

    @Override
    public void setMethodFunction(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        methodFns.computeIfAbsent(clazz, it -> new ConcurrentHashMap<>())
                .putIfAbsent(methodName, new MethodFunction(clazz, methodName, parameterTypes));
    }
}
