package org.dreamcat.round.el.ast;

import org.dreamcat.round.el.lex.CommentToken;
import org.dreamcat.round.el.lex.KeywordToken;
import org.dreamcat.round.el.lex.PunctuationToken;
import org.dreamcat.round.el.lex.Token;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * a thread-unsafe impl
 *
 * @author Jerry Will
 * @version 2021-07-23
 */
public interface BraceAnalyzer {

    static BraceNode analyse(TokenStream stream) {
        BraceNode root = new BraceNode();
        Token token;
        while (stream.hasNext()) {
            token = stream.next();
            if (KeywordToken.IF.equals(token)) {
                root.addChild(analyseIf(stream));
            } else if (KeywordToken.WHILE.equals(token)) {
                root.addChild(analyseWhile(stream));
            } else if (KeywordToken.FOR.equals(token)) {
                root.addChild(analyseFor(stream));
            } else if (KeywordToken.IMPORT.equals(token)) {
                root.addChild(InstructionAnalyzer.analyseImport(stream));
            } else if (PunctuationToken.RIGHT_BRACE.equals(token)) {
                stream.previous();
                break;
            } else if (PunctuationToken.SEMICOLON.equals(token) || token instanceof CommentToken) {
                // nop
            } else {
                stream.previous();
                ElNode node = SnippetAnalyzer.analyse(stream);
                if (node == null) return stream.throwWrongSyntax();
                root.addChild(node);
            }
        }
        return root;
    }

    static IfNode analyseIf(TokenStream stream) {
        if (!PunctuationToken.LEFT_PARENTHESIS.equals(stream.next())) {
            return stream.throwWrongSyntax();
        }

        IfNode node = new IfNode();
        node.cond = SnippetAnalyzer.analyse(stream);
        if (!PunctuationToken.RIGHT_PARENTHESIS.equals(stream.next())) {
            return stream.throwWrongSyntax();
        }
        boolean needBrace = false;
        if (PunctuationToken.LEFT_BRACE.equals(stream.next())) {
            node.thenPart = analyse(stream);
            needBrace = true;
        } else {
            stream.previous();
            node.thenPart = SnippetAnalyzer.analyse(stream);
        }

        Token token = stream.next();
        if ((needBrace && !PunctuationToken.RIGHT_BRACE.equals(token)) ||
                (!needBrace && !PunctuationToken.SEMICOLON.equals(token))) {
            return stream.throwWrongSyntax();
        }
        if (!KeywordToken.ELSE.equals(stream.next())) {
            stream.previous();
            return node;
        }

        if (KeywordToken.IF.equals(stream.next())) {
            node.elsePart = analyseIf(stream);
            needBrace = false;
        } else if (PunctuationToken.LEFT_BRACE.equals(stream.get())) {
            node.elsePart = analyse(stream);
            needBrace = true;
        } else {
            stream.previous();
            node.elsePart = SnippetAnalyzer.analyse(stream);
            needBrace = false;
        }

        if (needBrace) {
            if (!PunctuationToken.RIGHT_BRACE.equals(stream.next())) {
                return stream.throwWrongSyntax();
            }
        }
        return node;
    }

    static WhileNode analyseWhile(TokenStream stream) {
        if (!PunctuationToken.LEFT_PARENTHESIS.equals(stream.next())) {
            return stream.throwWrongSyntax();
        }

        WhileNode node = new WhileNode();
        node.cond = SnippetAnalyzer.analyse(stream);
        if (!PunctuationToken.RIGHT_PARENTHESIS.equals(stream.next())) {
            return stream.throwWrongSyntax();
        }
        boolean needBrace = false;
        if (PunctuationToken.LEFT_BRACE.equals(stream.next())) {
            node.body = analyse(stream);
            needBrace = true;
        } else {
            stream.previous();
            node.body = SnippetAnalyzer.analyse(stream);
        }
        Token token = stream.next();
        if ((needBrace && !PunctuationToken.RIGHT_BRACE.equals(token)) ||
                (!needBrace && !PunctuationToken.SEMICOLON.equals(token))) {
            return stream.throwWrongSyntax();
        }
        return node;
    }

    static ForNode analyseFor(TokenStream stream) {
        if (!PunctuationToken.LEFT_PARENTHESIS.equals(stream.next())) {
            return stream.throwWrongSyntax();
        }
        ForNode node = new ForNode();
        boolean hasPre = false;
        if (PunctuationToken.SEMICOLON.equals(stream.next())) {
            node.cond = SnippetAnalyzer.analyse(stream);
        } else {
            stream.previous();
            node.pre = SnippetAnalyzer.analyse(stream);
            hasPre = true;
        }
        if (!PunctuationToken.SEMICOLON.equals(stream.next())) {
            return stream.throwWrongSyntax();
        }
        // pre;     or      ;cond;
        if (PunctuationToken.RIGHT_PARENTHESIS.equals(stream.next())) {
            if (hasPre) {
                return stream.throwWrongSyntax();
            }
            // ;cond;)
        } else {
            // pre;cond;
            stream.previous();
            if (hasPre) {
                node.cond = SnippetAnalyzer.analyse(stream);
                if (!PunctuationToken.SEMICOLON.equals(stream.next())) {
                    return stream.throwWrongSyntax();
                }
                // pre;cond;post)       or      pre;cond;)
                if (!PunctuationToken.RIGHT_PARENTHESIS.equals(stream.next())) {
                    stream.previous();
                    node.post = SnippetAnalyzer.analyse(stream);
                    if (!PunctuationToken.RIGHT_PARENTHESIS.equals(stream.next())) {
                        return stream.throwWrongSyntax();
                    }
                }
            }
            // ;cond;post
            else {
                node.post = SnippetAnalyzer.analyse(stream);
                if (!PunctuationToken.RIGHT_PARENTHESIS.equals(stream.next())) {
                    return stream.throwWrongSyntax();
                }
            }
        }

        // for(xxx) xxx;
        if (!PunctuationToken.LEFT_BRACE.equals(stream.next())) {
            stream.previous();
            node.body = SnippetAnalyzer.analyse(stream);
            if (!PunctuationToken.SEMICOLON.equals(stream.next())) {
                return stream.throwWrongSyntax();
            }
            return node;
        }
        // for(xxx) { xxx }
        node.body = analyse(stream);
        if (!PunctuationToken.RIGHT_BRACE.equals(stream.next())) {
            return stream.throwWrongSyntax();
        }
        return node;
    }
}
