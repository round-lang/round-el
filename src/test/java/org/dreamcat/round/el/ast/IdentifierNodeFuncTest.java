package org.dreamcat.round.el.ast;

import java.math.BigDecimal;
import org.dreamcat.round.el.lex.OperatorToken;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-07-23
 */
class IdentifierNodeFuncTest extends NodeTest {

    @Test
    void test1() {
        evalNode("a(x).call().call() - b(b(b(y))).c(d)", engine -> {
            engine.getSettings().setEnableExtendedFunction(true);
            engine.setFunction("a", OperatorToken.SUB);
            engine.setExtendedFunction(BigDecimal.class, "call", (object, args) -> {
                double n = Math.pow(object.doubleValue(), 0.618);
                return BigDecimal.valueOf((long) n);
            });
            engine.setFunction("b", args ->
                    Math.sin(((Number) args[0]).doubleValue()));
            engine.setExtendedFunction(Double.class, "c", (object, args) ->
                    object * ((Number) args[0]).doubleValue());
        }, -2.130271, "x", new BigDecimal(1L << 32), "y", 1, "d", 3.14);
    }

    @Test
    void test_infix() {
        analyseAndShow("-a + +b() * -c(d)");
        analyseAndShow("a == 'N' || ((b / 3.14) > 2.72 && ('x' in c))");
    }

}
