package org.dreamcat.round.el.ast;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.function.BracketFunction;
import org.dreamcat.round.el.function.DotElFunction;
import org.dreamcat.round.el.function.ElFunction;
import org.dreamcat.round.el.function.MethodFunction;

/**
 * @author Jerry Will
 * @version 2021-07-24
 */
@Getter
@RequiredArgsConstructor
public class ChainNode extends TreeNode {

    @Override
    public ChainNode copy() {
        ChainNode node = new ChainNode();
        for (ElNode child : children) {
            node.addChild(child.copy());
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int size = children.size();
        if (size >= 1) {
            s.append(children.get(0));
        }
        for (int i = 1; i < size; i++) {
            s.append(".").append(children.get(i));
        }
        return s.toString();
    }

    @Override
    public Object evaluate(ElContext context, ElEngine engine) {
        int size = children.size();
        IdentifierNode child0 = (IdentifierNode) children.get(0);
        Object object = child0.evaluate(context, engine);
        for (int i = 1; i < size; i++) {
            ElNode node = children.get(i);
            if (node instanceof IdentifierNode) {
                IdentifierNode child = (IdentifierNode) node;
                if (child.children.isEmpty()) {
                    String property = child.getIdentifier();
                    object = DotElFunction.INSTANCE.invoke(object, property);
                } else {
                    object = evaluate(object, child, context, engine);
                }
            } else {
                BracketNode child = (BracketNode) node;
                Object value = child.evaluate(context, engine);
                object = BracketFunction.INSTANCE.invoke(object, value);
            }
        }
        return object;
    }

    private static Object evaluate(Object object, IdentifierNode node, ElContext context, ElEngine engine) {
        String functionName = node.getIdentifier();
        ParenthesisNode child = (ParenthesisNode) node.children.get(0);
        Object[] arguments = evaluateChildren(child.children, context, engine);

        Class<?>[] parameterTypes = getTypes(arguments);
        ElFunction function = null;
        if (engine.getSettings().isEnableExtendedFunction()) {
            function = engine.getExtendedFunction(object.getClass(), functionName);
        }
        if (function == null) {
            function = new MethodFunction(object, functionName, parameterTypes);
        }
        int n = arguments.length;
        Object[] newArguments = new Object[arguments.length + 1];
        newArguments[0] = object instanceof Class ? null : object;
        if (n > 0) {
            System.arraycopy(arguments, 0, newArguments, 1, n);
        }
        return function.invoke(newArguments);
    }
}
