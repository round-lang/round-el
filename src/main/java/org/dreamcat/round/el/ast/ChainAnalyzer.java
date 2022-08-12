package org.dreamcat.round.el.ast;

import org.dreamcat.common.util.ListUtil;
import org.dreamcat.round.lex.PunctuationToken;
import org.dreamcat.round.lex.Token;
import org.dreamcat.round.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2021-08-22
 */
public interface ChainAnalyzer {

    static ElNode analyse(TokenStream stream) {
        ChainNode root = new ChainNode();
        Token token;
        boolean needDot = false;
        while (stream.hasNext()) {
            token = stream.next();
            if (needDot) {
                if (token.isLeftParenthesis()) {
                    ElNode node = ParenthesisAnalyzer.analyse(stream);
                    if (!PunctuationToken.RIGHT_PARENTHESIS.equals(stream.next())) {
                        return stream.throwWrongSyntax();
                    }
                    ListUtil.getLast(root.children).addChild(node);
                } else if (token.isDot()) {
                    needDot = false;
                } else {
                    stream.previous();
                    break;
                }
            } else {
                if (token.isIdentifier()) {
                    ElNode node = new IdentifierNode(token.getIdentifier());
                    root.addChild(node);
                    needDot = true;
                } else {
                    stream.previous();
                    break;
                }
            }
        }
        if (root.children.size() == 1) {
            ElNode node = root.children.get(0);
            node.parent = null;
            return node;
        } else {
            return root;
        }
    }
}
