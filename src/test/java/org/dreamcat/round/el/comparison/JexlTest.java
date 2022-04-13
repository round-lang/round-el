package org.dreamcat.round.el.comparison;

import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.internal.Engine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
class JexlTest {

    static final JexlEngine engine = new Engine();
    static final String bool_expression1 = "a == 3 && ((b + 3.14) > 5 || (c - a * 2) / 3 <= 0)";
    static final String bool_expression2 = "a == 'N' || ((b / 3.14) > 2.72 && c.contains('x'))";
    static final String num_expression1 = "(a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6)";
    static final String num_expression2 = "(a + 3.16e-9) * ((b + 3.14E7) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6594162598152)";


    static final JexlContext context1 = new MapContext();
    static final JexlContext context2 = new MapContext();
    static final JexlContext context3 = new MapContext();

    @BeforeAll
    static void init() {
        context1.set("a", 3);
        context1.set("b", 1.414);
        context1.set("c", 6.32);

        context2.set("a", 3);
        context2.set("b", 0.618);
        context2.set("c", 5);

        context3.set("a", "N");
        context3.set("b", 9.11);
        context3.set("c", new String[]{"x", "y", "z"});
    }

    @Test
    void testBool1() {
        JexlExpression el = engine.createExpression(bool_expression1);
        Object r1 = el.evaluate(context1);
        assert r1.equals(false);
        Object r2 = el.evaluate(context2);
        assert r2.equals(true);
    }

    @Test
    void testBool2() {
        JexlExpression el = engine.createExpression(bool_expression2);
        Object r2 = el.evaluate(context3);
        assert r2.equals(true);
    }

    @Test
    void testNum1() {
        JexlExpression el = engine.createExpression(num_expression1);
        Object r1 = el.evaluate(context1);
        assert r1 instanceof Double;
        Object r2 = el.evaluate(context2);
        assert r2 instanceof Double;
    }

    @Test
    void testNum2() {
        JexlExpression el = engine.createExpression(num_expression2);
        Object r1 = el.evaluate(context1);
        assert r1 instanceof Double;
        Object r2 = el.evaluate(context2);
        assert r2 instanceof Double;
    }
}
