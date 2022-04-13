package org.dreamcat.round.el.lex;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
public class NumberToken extends ValueToken<Number> {

    public NumberToken(Number value, String rawToken) {
        super(value, Type.NUMBER, rawToken);
    }
}
