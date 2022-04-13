package org.dreamcat.round.el.ast;

import org.dreamcat.round.el.lex.KeywordToken;
import org.dreamcat.round.el.lex.OperatorToken;
import org.dreamcat.round.el.lex.Token;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2021-08-16
 */
public interface SnippetAnalyzer {

    static ElNode analyse(TokenStream stream) {
        ElNode root = null, parent = null, current = null;
        Token token;
        while (stream.hasNext()) {
            token = stream.next();
            if (token.isLeftParenthesis()) {
                ElNode node = ParenthesisAnalyzer.analyse(stream);
                if (!stream.next().isRightParenthesis()) {
                    return stream.throwWrongSyntax();
                }
                if (current != null) {
                    if (node == null) {
                        return stream.throwWrongSyntax();
                    } else {
                        current.addChild(node);
                    }
                } else if (parent != null) {
                    if (node == null) {
                        return stream.throwWrongSyntax();
                    }
                    parent.addChild(node);
                    current = node;
                } else {
                    root = current = node;
                }
            } else if (token.isIdentifier()) {
                ElNode node;
                if (KeywordToken.RETURN.equals(token)) {
                    node = InstructionAnalyzer.analyseReturn(stream);
                } else {
                    stream.previous();
                    node = ChainAnalyzer.analyse(stream);
                }
                // as child and a variable or prefix function
                if (current == null) {
                    current = node;
                    if (parent != null) {
                        parent.addChild(node);
                    } else {
                        root = node;
                    }
                    continue;
                }
                // as parent and an infix function
                if (!(node instanceof IdentifierNode)) {
                    stream.previous();
                    return stream.throwWrongSyntax();
                }
                if (parent != null) {
                    if (parent.isOperator()) {
                        ElNode pp;
                        while ((pp = parent.parent) != null && (pp.isOperator() &&
                                pp.getOperator().comparePriority(OperatorToken.IDENTIFIER))) {
                            parent = pp;
                        }
                        if (pp != null) {
                            node.join(pp, parent);
                        } else {
                            node.addChild(parent);
                            root = node;
                        }
                    } else {
                        node.join(parent, current);
                    }
                    parent = node;
                    current = null;
                } else {
                    current.setParent(node);
                    root = parent = node;
                    current = null;
                }
            } else if (token.isValue()) {
                ConstNode node = new ConstNode(token.getValue());
                if (current == null) {
                    current = node;
                    if (parent != null) {
                        parent.addChild(node);
                    } else {
                        root = node;
                    }
                } else {
                    return stream.throwWrongSyntax();
                }
            } else if (token.isOperator()) {
                OperatorToken operator = token.getOperator();
                OperatorNode node = new OperatorNode(operator);
                if (current != null) {
                    if (parent == null) {
                        root.setParent(node);
                        root = node;
                    } else if (parent.isOperator() &&
                            (parent.getOperator().comparePriority(operator))) {
                        ElNode pp;
                        while ((pp = parent.parent) != null && pp.isOperator() &&
                                pp.getOperator().comparePriority(operator)) {
                            parent = pp;
                        }
                        if (pp != null) {
                            node.join(pp, parent);
                        } else {
                            node.addChild(root);
                            root = node;
                        }
                    } /*else if (operator.equals(OperatorToken.NOT)) {
                        // case a!
                    } */ else {
                        node.join(parent, current);
                    }
                } else if (parent != null) {
                    parent.addChild(node);
                } else {
                    root = node;
                }

                parent = node;
                current = null;
            } else if (token.isRightParenthesis() || token.isRightBracket() ||
                    token.isLeftBrace() || token.isRightBrace() ||
                    token.isComma() || token.isSemicolon()) {
                stream.previous();
                break;
            } else if (token.isLeftBracket()) {
                BracketNode node = BracketAnalyzer.analyse(stream);

                // case a[1..], a(1)[1], (a + b)[0], a[0][0]
                if (current != null) {
                    current.addChild(node);
                    parent = current;
                    current = node;
                }
                // case a + [1]
                else if (parent != null) {
                    parent.addChild(node);
                    current = node;
                } else {
                    root = current = node;
                }
            } else {
                return stream.throwWrongSyntax();
            }
        }

        return root;
    }

}
