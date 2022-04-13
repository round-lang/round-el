package org.dreamcat.round.el.lex;

/**
 * @author Jerry Will
 * @version 2021-08-30
 */
public interface TokenInfo {

    // token in the expression
    Token getToken();

    // start offset in the expression
    int getStart();

    // end offset in the expression
    int getEnd();

    // which line
    int getLine();

    // which column
    int getCol();
}
