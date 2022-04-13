package org.dreamcat.round.el.ast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.exception.ExecuteException;
import org.dreamcat.round.el.exception.ReturnException;
import org.dreamcat.round.el.function.BracketFunction;
import org.dreamcat.round.el.function.ConstructorFunction;
import org.dreamcat.round.el.function.ElFunction;
import org.dreamcat.round.el.lex.KeywordToken;
import org.dreamcat.round.el.lex.OperatorToken;

/**
 * @author Jerry Will
 * @since 2021-07-09
 */
@Getter
@RequiredArgsConstructor
public class IdentifierNode extends TreeNode {

    public final String identifier;

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    OperatorToken getOperator() {
        return OperatorToken.IDENTIFIER;
    }

    @Override
    public String toString() {
        if (children.isEmpty()) return identifier;
        StringBuilder s = new StringBuilder();
        s.append(identifier);
        int size = children.size();
        ElNode child;
        if (size == 1 && (child = children.get(0)) instanceof BracketNode) {
            return s.append(child).toString();
        }
        s.append('(').append(children.get(0));
        for (int i = 1; i < size; i++) {
            s.append(", ").append(children.get(i));
        }
        return s.append(')').toString();
    }

    @Override
    public ElNode copy() {
        IdentifierNode node = new IdentifierNode(identifier);
        for (ElNode child : children) {
            node.children.add(child.copy());
        }
        return node;
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        // as a variable
        if (children.isEmpty()) {
            return evaluate(identifier, context, engine);
        }

        if (KeywordToken.RETURN.is(identifier)) {
            Object returnValue = children.get(0).evaluate(context, engine);
            throw new ReturnException(returnValue);
        }

        ElNode child = children.get(0);
        if (children.size() == 1 && child instanceof ParenthesisNode) {
            children = ((ParenthesisNode) child).children;
        }

        // case a[...]
        if (children.size() == 1 && child instanceof BracketNode) {
            Object object = context.get(identifier);
            Object index = child.evaluate(context, engine);
            return BracketFunction.INSTANCE.invoke(object, index);
        }

        Object[] arguments = evaluateChildren(children, context, engine);
        Class<?>[] parameterTypes = getTypes(arguments);
        // case a(...)
        ElFunction function = engine.getFunction(identifier);
        if (function == null) {
            Object value = context.get(identifier);
            if (value instanceof Class) {
                function = new ConstructorFunction((Class<?>) value, parameterTypes);
            } else {
                throw new ExecuteException(String.format("no such function %s", identifier));
            }
        }
        return function.invoke(arguments);
    }
}
