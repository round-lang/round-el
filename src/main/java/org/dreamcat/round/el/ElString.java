package org.dreamcat.round.el;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Create by tuke on 2020/10/26
 */
@SuppressWarnings("unchecked")
public interface ElString {

    /**
     * get raw expression
     *
     * @return raw expression
     */
    String getExpression();

    ElEngine getEngine();

    /**
     * evaluate partially
     *
     * @param context context which holds the variables
     * @return partial evaluated el
     */
    default ElString optimize(ElContext context) {
        throw new UnsupportedOperationException();
    }

    default Object evaluate() {
        return evaluate(ElContext.of());
    }

    /**
     * evaluate for <strong>Expression Language</strong>
     *
     * @param context context which holds the variables
     * @return evaluated result
     * @throws NoSuchElementException if any variable is not found in context
     */
    Object evaluate(ElContext context);

    default boolean evaluateAsBool() {
        return Objects.equals(evaluate(), true);
    }

    default boolean evaluateAsBool(ElContext context) {
        return Objects.equals(evaluate(context), true);
    }

    default <T> T evaluateAs() {
        return (T) evaluate();
    }

    default <T> T evaluateAs(ElContext context) {
        return (T) evaluate(context);
    }
}
