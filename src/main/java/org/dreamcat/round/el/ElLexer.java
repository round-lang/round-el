package org.dreamcat.round.el;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.dreamcat.round.lex.IdentifierToken;
import org.dreamcat.round.lex.LexConfig;
import org.dreamcat.round.lex.LexConfig.BigNumberStrategy;
import org.dreamcat.round.lex.Lexer;
import org.dreamcat.round.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2022-08-12
 */
public class ElLexer {

    private final LexConfig config = new LexConfig();
    private final Lexer lexer = new Lexer(config);

    public ElLexer(ElConfig elConfig) {
        config.setKeywords(keywordUMap);

        if (elConfig.isEnableBigNumber()) {
            config.setBigNumberStrategy(BigNumberStrategy.RANGE);
        }
        int sampleCharCount = elConfig.getSampleCharCount();
        if (sampleCharCount > 0) {
            config.setSampleCharCount(sampleCharCount);
        }
    }

    public void clear() {
        lexer.clear();
    }

    public TokenStream lex(String expression) {
        return lexer.lex(expression);
    }

    public static final IdentifierToken AS = addKeyword("as");
    public static final IdentifierToken BREAK = addKeyword("break");
    public static final IdentifierToken CLASS = addKeyword("class");
    public static final IdentifierToken CATCH = addKeyword("catch");
    public static final IdentifierToken CONTINUE = addKeyword("continue");
    public static final IdentifierToken END = addKeyword("end");
    public static final IdentifierToken ELIF = addKeyword("elif");
    public static final IdentifierToken ELSE = addKeyword("else");
    public static final IdentifierToken FOR = addKeyword("for");
    public static final IdentifierToken FALSE = addKeyword("false");
    public static final IdentifierToken FINALLY = addKeyword("finally");
    public static final IdentifierToken IF = addKeyword("if");
    public static final IdentifierToken IMPORT = addKeyword("import");
    public static final IdentifierToken INSTANCEOF = addKeyword("instanceof");
    public static final IdentifierToken IS = addKeyword("is");
    public static final IdentifierToken IN = addKeyword("in");
    public static final IdentifierToken NULL = addKeyword("null");
    public static final IdentifierToken RETURN = addKeyword("return");
    public static final IdentifierToken THROW = addKeyword("throw");
    public static final IdentifierToken TRUE = addKeyword("true");
    public static final IdentifierToken WHILE = addKeyword("while");

    private static final Map<String, IdentifierToken> keywordMap = new HashMap<>();
    private static final Map<String, IdentifierToken> keywordUMap =
            Collections.unmodifiableMap(keywordMap);

    private static IdentifierToken addKeyword(String keyword) {
        IdentifierToken token = new IdentifierToken(keyword);
        keywordMap.put(keyword, token);
        return token;
    }
}
