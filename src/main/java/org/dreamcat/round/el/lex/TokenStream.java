package org.dreamcat.round.el.lex;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public interface TokenStream {

    /**
     * has next
     */
    boolean hasNext();

    /**
     * get next token: ++offset
     */
    Token next();

    /**
     * get previous token: --offset
     */
    Token previous();

    /**
     * get current token: offset
     * must call {@link #next()} first
     */
    Token get();

    default TokenInfo getTokenInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * reset the stream
     */
    void reset();

    // throws an exception at current offset
    <T> T throwWrongSyntax();
}
