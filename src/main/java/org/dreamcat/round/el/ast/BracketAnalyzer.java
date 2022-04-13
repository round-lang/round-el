package org.dreamcat.round.el.ast;

import org.dreamcat.round.el.lex.PunctuationToken;
import org.dreamcat.round.el.lex.Token;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2021-08-15
 */
public interface BracketAnalyzer {

    static BracketNode analyse(TokenStream stream) {
        BracketNode root = new BracketNode();
        Token token;
        while (stream.hasNext()) {
            ElNode element = SnippetAnalyzer.analyse(stream);
            if (element == null) break;
            root.addChild(element);

            token = stream.next();
            if (PunctuationToken.RIGHT_BRACKET.equals(token)) {
                stream.previous();
                break;
            } else if (PunctuationToken.SEMICOLON.equals(token)) {
                root.nextRow();
                if (PunctuationToken.RIGHT_BRACKET.equals(stream.next())) {
                    stream.previous();
                    break;
                }
                stream.previous();
            } else if (!PunctuationToken.COMMA.equals(token)) {
                return stream.throwWrongSyntax();
            }
        }
        if (!stream.next().isRightBracket()) {
            return stream.throwWrongSyntax();
        }
        root.clearRow();
        return root;
    }

}
