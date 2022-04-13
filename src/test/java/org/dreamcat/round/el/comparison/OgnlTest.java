package org.dreamcat.round.el.comparison;

import java.util.HashMap;
import java.util.Map;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
class OgnlTest {

    static final String bool_expression = "a == 3 && ((b + 3.14) > 5 || (c - a * 2) / 3 <= 0)";
    static final String num_expression1 = "(a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6)";
    static final String num_expression2 = "(a + 3.16e-9) * ((b + 3.14E7) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6594162598152)";
    static final Map<String, Object> context1 = new HashMap<>();
    static final Map<String, Object> context2 = new HashMap<>();

    @BeforeAll
    static void init() {
        context1.put("a", 3);
        context1.put("b", 1.414);
        context1.put("c", 6.32);

        context2.put("a", 3);
        context2.put("b", 0.618);
        context2.put("c", 5);
    }

    @Test
    void testBool1() throws OgnlException {
        Object r1 = Ognl.getValue(bool_expression, null, context1);
        assert r1.equals(false);

        Object r2 = Ognl.getValue(bool_expression, null, context2);
        assert r2.equals(true);
    }

    @Test
    void testBool2() throws OgnlException {
        Node node = (Node) Ognl.parseExpression(bool_expression);
        OgnlContext ognlContext = (OgnlContext) Ognl.createDefaultContext(null);
        Object r1 = node.getValue(ognlContext, context1);
        assert r1.equals(false);

        Object r2 = node.getValue(ognlContext, context2);
        assert r2.equals(true);
    }

    @Test
    void testNum1() throws OgnlException {
        Node node = (Node) Ognl.parseExpression(num_expression1);
        OgnlContext ognlContext = (OgnlContext) Ognl.createDefaultContext(null);
        Object r1 = node.getValue(ognlContext, context1);
        assert r1 instanceof Double;

        Object r2 = node.getValue(ognlContext, context2);
        assert r2 instanceof Double;
    }

    @Test
    void testNum2() {
        Assertions.assertThrows(NumberFormatException.class, () -> {
            Ognl.parseExpression(num_expression2);
        });
    }

    @Test
    void testObject() throws OgnlException {
        Node node = (Node) Ognl.parseExpression("a(x).call().call() == b(b(b(y)))");
        assert node != null;
        node = (Node) Ognl.parseExpression("log.error(e.getMessage(), e, System.out.println())");
        assert node != null;
    }
}
