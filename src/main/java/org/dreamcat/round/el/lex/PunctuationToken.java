package org.dreamcat.round.el.lex;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
@Getter
@RequiredArgsConstructor
public enum PunctuationToken implements Token {
    COMMA(","),
    SEMICOLON(";"),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    ;

    final String raw;

    @Override
    public Type getType() {
        return Type.PUNCTUATION;
    }

    @Override
    public boolean isLeftParenthesis() {
        return equals(LEFT_PARENTHESIS);
    }

    @Override
    public boolean isRightParenthesis() {
        return equals(RIGHT_PARENTHESIS);
    }

    @Override
    public boolean isLeftBracket() {
        return equals(LEFT_BRACKET);
    }

    @Override
    public boolean isRightBracket() {
        return equals(RIGHT_BRACKET);
    }

    @Override
    public boolean isLeftBrace() {
        return equals(LEFT_BRACE);
    }

    @Override
    public boolean isRightBrace() {
        return equals(RIGHT_BRACE);
    }

    @Override
    public boolean isSemicolon() {
        return equals(SEMICOLON);
    }

    @Override
    public boolean isComma() {
        return equals(COMMA);
    }

    public static PunctuationToken search(char c) {
        return valueMap.get(c);
    }

    private static final Map<Character, PunctuationToken> valueMap = new HashMap<>();

    static {
        for (PunctuationToken value : values()) {
            valueMap.put(value.raw.charAt(0), value);
        }
    }
}
