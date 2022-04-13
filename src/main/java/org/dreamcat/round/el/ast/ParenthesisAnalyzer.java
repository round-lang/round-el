package org.dreamcat.round.el.ast;

import org.dreamcat.round.el.lex.PunctuationToken;
import org.dreamcat.round.el.lex.Token;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2021-08-15
 */
public interface ParenthesisAnalyzer {

    static ParenthesisNode analyse(TokenStream stream) {
        Token token;
        ParenthesisNode root = new ParenthesisNode();
        while (stream.hasNext()) {
            ElNode child = SnippetAnalyzer.analyse(stream);
            if (child == null) {
                break;
            }
            root.addChild(child);
            token = stream.next();
            if (PunctuationToken.RIGHT_PARENTHESIS.equals(token)) {
                stream.previous();
                break;
            } else if (PunctuationToken.COMMA.equals(token)) {
                if (PunctuationToken.RIGHT_PARENTHESIS.equals(stream.get())) {
                    break;
                }
            } else {
                return stream.throwWrongSyntax();
            }
        }

        return root;
    }

}
