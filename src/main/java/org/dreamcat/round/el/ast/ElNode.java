package org.dreamcat.round.el.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.exception.BreakException;
import org.dreamcat.round.el.exception.ContinueException;
import org.dreamcat.round.el.lex.KeywordToken;
import org.dreamcat.round.el.lex.OperatorToken;
import org.dreamcat.round.el.lex.TokenStream;

/**
 * @author Jerry Will
 * @version 2021-08-01
 */
public abstract class ElNode {

    public ElNode parent;

    public static ElNode compile(TokenStream stream) {
        return BraceAnalyzer.analyse(stream);
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    public abstract ElNode copy();

    public void optimize() {
        // nop
    }

    public Object evaluate(ElContext context, ElEngine engine) {
        throw new UnsupportedOperationException(getClass().getName());
    }

    static Object[] evaluateChildren(List<ElNode> children, ElContext context, ElEngine engine) {
        int size = children.size();
        Object[] arguments = new Object[size];
        for (int i = 0; i < size; i++) {
            arguments[i] = children.get(i).evaluate(context, engine);
        }
        return arguments;
    }

    static Object evaluate(String identifier, ElContext context, ElEngine engine) {
        // control chars
        if (KeywordToken.BREAK.is(identifier)) {
            throw new BreakException();
        } else if (KeywordToken.CONTINUE.is(identifier)) {
            throw new ContinueException();
        }
        Object value = context.get(identifier);
        if (value == null) {
            value = engine.getObject(identifier);
        }
        return value;
    }

    static Class<?>[] getTypes(Object[] arguments) {
        return Arrays.stream(arguments).map(it -> it != null ? it.getClass() : null)
                .toArray(Class[]::new);
    }

    @Override
    public abstract String toString();

    public String toTreeString() {
        StringBuilder s = new StringBuilder(1 << 8);
        s.append(toString());
        if (parent != null) {
            s.append("  <--  ").append(parent.toString());
        }

        List<ElNode> current = Collections.emptyList();
        List<ElNode> next;
        if (this instanceof TreeNode) current = ((TreeNode) this).children;
        while (!current.isEmpty()) {
            s.append("\n");
            next = new ArrayList<>();
            for (ElNode node : current) {
                s.append(String.format("%10s", node.toString()));
                if (node instanceof TreeNode) {
                    next.addAll(((TreeNode) node).children);
                }
            }
            current = next;
        }
        return s.toString();
    }

    // ---- ---- ---- ----    ---- ---- ---- ----    ---- ---- ---- ----

    void addChild(ElNode child) {
        throw new UnsupportedOperationException();
    }

    void removeChild(ElNode child) {
        throw new UnsupportedOperationException();
    }

    void setParent(ElNode node) {
        this.parent = node;
        node.addChild(this);
    }

    void join(ElNode parent, ElNode child) {
        parent.removeChild(child);
        parent.addChild(this);
        this.addChild(child);
    }

    boolean isOperator() {
        return false;
    }

    OperatorToken getOperator() {
        throw new UnsupportedOperationException();
    }
}
