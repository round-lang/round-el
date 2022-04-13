package org.dreamcat.round.el.ast;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.dreamcat.common.util.NumberUtil;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.ElSettings;
import org.dreamcat.round.el.TestBase;
import org.dreamcat.round.el.lex.Lexer;

/**
 * @author Jerry Will
 * @version 2021-07-23
 */
public class NodeTest extends TestBase {

    public void evalNode(String expression, Object expect, Object... input) {
        evalNode(expression, null, expect, input);
    }

    public void evalNode(String expression, ElContext context, Object expect) {
        evalNode(expression, null, context, expect);
    }

    public void evalNode(String expression, Consumer<ElEngine> configurator, Object expect, Object... input) {
        evalNode(expression, configurator, it -> test_equals.test(it, expect), input);
    }

    public void evalNode(String expression, Consumer<ElEngine> configurator, ElContext context, Object expect) {
        evalNode(expression, configurator, context, it -> test_equals.test(it, expect));
    }

    public void evalNode(String expression, Predicate<Object> test, Object... input) {
        evalNode(expression, null, test, input);
    }

    public void evalNode(String expression, Consumer<ElEngine> configurator, Predicate<Object> test, Object... input) {
        ElContext context = ElContext.createContext();
        int size = input.length;
        for (int i = 0; i < size; i += 2) {
            context.set(input[i].toString(), input[i + 1]);
        }
        evalNode(expression, configurator, context, test);
    }

    public void evalNode(String expression, Consumer<ElEngine> configurator, ElContext context,
            Predicate<Object> test) {
        ElEngine engine = ElEngine.getEngine();
        if (configurator != null) configurator.accept(engine);
        BraceNode root = analyse(expression);
        Object result = root.evaluate(context, engine);
        System.out.printf("result \t = \t %s %n", result);
        assert test.test(result);
        System.out.println();
    }

    BraceNode analyse(String expression) {
        System.out.printf("expression \t = \t %s %n", expression);
        Lexer lexer = new Lexer(new ElSettings());
        return BraceAnalyzer.analyse(lexer.lex(expression));
    }

    public void analyseAndShow(String expression) {
        BraceNode root = analyse(expression);
        System.out.println(root);
        System.out.println();
        for (ElNode child : root.children) {
            System.out.println(child);
            System.out.println();
        }
    }

    public static class A {

        A a = this;
        int b = 0;

        public A a() {
            return a(1);
        }

        public A a(int diff) {
            b += diff;
            return a;
        }

        public int b() {
            return b * 2;
        }

        public Integer depth() {
            return b * b * b;
        }
    }

    private static final BiPredicate<Object, Object> test_equals = (result, expect) -> {
        if (expect instanceof Number && NumberUtil.isFloatLike((Number) expect)) {
            return NumberUtil.almostEq(
                    ((Number) result).doubleValue(),
                    ((Number) expect).doubleValue(), 4);
        }
        if (Objects.equals(result, expect)) return true;
        else {
            System.err.printf("result \t = \t %s, expect \t = \t %s %n", result, expect);
            return false;
        }
    };
}
