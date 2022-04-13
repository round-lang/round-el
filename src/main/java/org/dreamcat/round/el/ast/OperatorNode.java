package org.dreamcat.round.el.ast;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.common.util.ArrayUtil;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.exception.ExecuteException;
import org.dreamcat.round.el.function.BracketSetFunction;
import org.dreamcat.round.el.function.DotElFunction;
import org.dreamcat.round.el.function.ElFunction;
import org.dreamcat.round.el.lex.OperatorToken;

/**
 * @author Jerry Will
 * @since 2021-07-09
 */
@Getter
@RequiredArgsConstructor
public class OperatorNode extends TreeNode {

    public final OperatorToken operator;

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public String toString() {
        int size = children.size();
        StringBuilder s = new StringBuilder();
        if (size == 1) {
            s.append(operator.getRawToken());
            s.append(" ").append(children.get(0));
        } else if (size == 2) {
            s.append(children.get(0)).append(" ");
            s.append(operator.getRawToken());
            s.append(" ").append(children.get(1));
        } else {
            s.append(operator.getRawToken());
        }
        return s.toString();
    }

    @Override
    public ElNode copy() {
        OperatorNode node = new OperatorNode(operator);
        for (ElNode child : children) {
            node.addChild(child.copy());
        }
        return node;
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        if (operator.isDot()) {
            return evalDot(context, engine);
        }

        if (operator.isShortCircuit()) {
            Object left = children.get(0).evaluate(context, engine);
            if (Objects.equals(left, false) && operator.equals(OperatorToken.AND)) {
                return false;
            }
            return children.get(1).evaluate(context, engine);
        }

        if (operator.equals(OperatorToken.ASSIGN)) {
            TreeNode left = (TreeNode) children.get(0);
            ElNode right = children.get(1);
            Object rightValue = right.evaluate(context, engine);

            if (left instanceof IdentifierNode) {
                if (!left.children.isEmpty()) {
                    if (left.children.size() != 1) {
                        throw new ExecuteException("no impl: " + this);
                    }
                    ElNode leftChild = left.children.get(0);
                    Object leftIndex = leftChild.evaluate(context, engine);
                    Object object = evaluate(((IdentifierNode) left).getIdentifier(),
                            context, engine);
                    return BracketSetFunction.INSTANCE.invoke(object, leftIndex, rightValue);
                }
                context.set(((IdentifierNode) left).getIdentifier(), rightValue);
            } else {
                throw new ExecuteException("no impl: " + this);
            }
            return rightValue;
        }

        Object[] arguments = evaluateChildren(children, context, engine);

        ElFunction function = null;
        if (arguments.length == 2) {
            function = engine.getRegisteredFunction(operator, arguments[0].getClass(), arguments[1].getClass());
        }
        if (function == null) {
            function = engine.getFunction(operator);
        }
        return function.invoke(arguments);
    }

    private Object evalDot(ElContext context, ElEngine engine) {
        ElNode object = children.get(0);
        Object objectValue = object.evaluate(context, engine);
        Class<?> objectClass = objectValue.getClass();

        IdentifierNode property = (IdentifierNode) children.get(1);
        String propertyValue = property.getIdentifier();

        // treat as a property
        if (property.children.isEmpty()) {
            return DotElFunction.INSTANCE.invoke(objectValue, propertyValue);
        }

        // treat as a function
        ElFunction function = engine.getMethodFunction(objectClass, propertyValue);
        if (function == null) {
            if (engine.getSettings().isEnableExtendedFunction()) {
                function = engine.getExtendedFunction(objectClass, propertyValue);
            }
            if (function == null) {
                throw new ExecuteException(String.format(
                        "no such function `%s` of %s", propertyValue, objectClass));
            }
        }

        Object[] arguments = evaluateChildren(property.children, context, engine);
        Object[] newArguments = ArrayUtil.concat(new Object[]{objectValue}, arguments);
        return function.invoke(newArguments);
    }
}
