package org.dreamcat.round.el.ast;

import java.util.ArrayList;
import java.util.List;
import org.dreamcat.round.el.exception.CompileException;
import org.dreamcat.round.el.lex.KeywordToken;
import org.dreamcat.round.el.lex.PunctuationToken;
import org.dreamcat.round.el.lex.Token;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2021-08-15
 */
public interface InstructionAnalyzer {

    static ImportNode analyseImport(TokenStream stream) {
        List<String> className = new ArrayList<>();
        Token token;
        boolean needDot = false;
        while (stream.hasNext() && !PunctuationToken.SEMICOLON.equals(token = stream.next())) {
            if (needDot) {
                if (!token.isDot()) {
                    throw new CompileException("invalid import statement");
                }
                needDot = false;
            } else {
                if (!token.isIdentifier()) {
                    throw new CompileException("invalid import statement");
                }
                needDot = true;
                className.add(token.getIdentifier());
            }
        }
        return ImportNode.valueOf(String.join(".", className));
    }

    static IdentifierNode analyseReturn(TokenStream tokens) {
        IdentifierNode returnNode = new IdentifierNode(KeywordToken.RETURN.getIdentifier());
        ElNode value = SnippetAnalyzer.analyse(tokens);
        returnNode.addChild(value);
        return returnNode;
    }
}
