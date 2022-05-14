package org.dreamcat.round.el.lex;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

/**
 * @author Jerry Will
 * @version 2022-01-26
 */
@RequiredArgsConstructor
public enum KeywordToken implements Token {

    AS("as"),
    BREAK("break"),
    CLASS("class"),
    CATCH("catch"),
    CONTINUE("continue"),
    END("end"),
    ELIF("elif"),
    ELSE("else"),
    FOR("for"),
    FALSE("false"),
    FINALLY("finally"),
    IF("if"),
    IMPORT("import"),
    INSTANCEOF("instanceof"),
    IS("is"),
    IN("in"),
    RETURN("return"),
    THROW("throw"),
    TRUE("true"),
    WHILE("while"),
    ;

    private final String keyword;

    private static final Map<String, KeywordToken> keywordCache;

    static {
        keywordCache = new HashMap<>();
        for (KeywordToken value : values()) {
            keywordCache.put(value.keyword, value);
        }
    }

    public static KeywordToken of(String keyword) {
        return keywordCache.get(keyword);
    }

    @Override
    public Type getType() {
        return Type.IDENTIFIER;
    }

    @Override
    public boolean isIdentifier() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return keyword;
    }

    @Override
    public String getRaw() {
        return keyword;
    }

    public boolean is(String identifier) {
        return this.keyword.equals(identifier);
    }
}
