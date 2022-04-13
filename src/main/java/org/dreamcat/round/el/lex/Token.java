package org.dreamcat.round.el.lex;

/**
 * @author Jerry Will
 * @since 2021-07-03
 */
public interface Token {

    Type getType();

    String getRawToken();

    default boolean isIdentifier() {
        return false;
    }

    default String getIdentifier() {
        throw new UnsupportedOperationException();
    }

    default boolean isValue() {
        return false;
    }

    default Object getValue() {
        throw new UnsupportedOperationException();
    }

    default boolean isOperator() {
        return false;
    }

    default OperatorToken getOperator() {
        throw new UnsupportedOperationException();
    }

    default boolean isLeftParenthesis() {
        return false;
    }

    default boolean isRightParenthesis() {
        return false;
    }

    default boolean isLeftBracket() {
        return false;
    }

    default boolean isRightBracket() {
        return false;
    }

    default boolean isLeftBrace() {
        return false;
    }

    default boolean isRightBrace() {
        return false;
    }

    default boolean isSemicolon() {
        return false;
    }

    default boolean isComma() {
        return false;
    }

    default boolean isDot() {
        return false;
    }

    enum Type {
        IDENTIFIER,
        STRING,
        NUMBER,
        OPERATOR,
        PUNCTUATION,
        COMMENT,
    }
}