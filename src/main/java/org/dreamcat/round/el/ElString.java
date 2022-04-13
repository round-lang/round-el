package org.dreamcat.round.el;

import java.util.NoSuchElementException;

/**
 * Create by tuke on 2020/10/26
 */
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
        return evaluate(ElContext.createContext());
    }

    /**
     * evaluate for <strong>Expression Language</strong>
     *
     * @param context context which holds the variables
     * @return evaluated result
     * @throws NoSuchElementException if any variable is not found in context
     */
    Object evaluate(ElContext context);

}
