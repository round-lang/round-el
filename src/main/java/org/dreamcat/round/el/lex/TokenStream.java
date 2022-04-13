package org.dreamcat.round.el.lex;

/**
 * @author Jerry Will
 * @since 2021-07-04
 */
public interface TokenStream {

    // get current token info: offset
    TokenInfo getTokenInfo();

    // get current token: offset
    Token get();

    // has next
    boolean hasNext();

    // get next token: ++offset
    Token next();

    // get previous token: --offset
    void previous();

    TokenStream nextUntil(Token delimiterToken);

    // throws an exception at current offset
    <T> T throwWrongSyntax();
}
