package org.dreamcat.round.el;

import java.util.function.BiFunction;
import org.dreamcat.round.el.function.ElFunction;
import org.dreamcat.round.el.lex.OperatorToken;

/**
 * Create by tuke on 2020/10/26
 */
public interface ElEngine {

    /**
     * expensive but thread-safe instance, maybe make a cache for it
     *
     * @return a impl of {@link ElEngine}
     */
    static ElEngine getEngine() {
        return new SimpleElEngine();
    }

    /**
     * compile to el
     *
     * @param expression el
     * @return compiled el
     */
    ElString compile(String expression);

    /**
     * evaluate el directly
     *
     * @param expression el
     * @return result
     */
    default Object evaluate(String expression) {
        return evaluate(expression, ElContext.createContext());
    }

    default Object evaluate(String expression, ElContext context) {
        return compile(expression).evaluate(context);
    }

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    ElSettings getSettings();

    // ==== ==== ==== ====    ==== ==== ==== ====    ==== ==== ==== ====

    Object getObject(String name);

    void setObject(String name, Object value);

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    ElFunction getFunction(String name);

    ElFunction getFunction(OperatorToken operator);

    void setFunction(OperatorToken operator, ElFunction function);

    void setFunction(String name, ElFunction function);

    void setFunction(String name, OperatorToken operator);

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    <T, R> ElFunction getRegisteredFunction(OperatorToken operator, Class<T> left, Class<R> right);

    ElFunction getExtendedFunction(Class<?> clazz, String methodName);

    ElFunction getMethodFunction(Class<?> clazz, String methodName);

    <T, R> void setRegisterFunction(OperatorToken operator,
            Class<T> left, Class<R> right, BiFunction<T, R, Object> fn);

    <T> void setExtendedFunction(Class<T> clazz, String name,
            BiFunction<T, Object[], Object> fn);

    void setMethodFunction(Class<?> clazz, String methodName, Class<?>... parameterTypes);
}