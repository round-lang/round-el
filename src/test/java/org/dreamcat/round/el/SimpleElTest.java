package org.dreamcat.round.el;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
class SimpleElTest extends TestBase {

    static final String bool_expression1 = "a == 3 && ((b + 3.14) > 5 || (c - a * 2) / 3 <= 0)";
    static final String bool_expression2 = "a == 'N' || ((b / 3.14) > 2.72 && ('x' in c))";
    /*
                                        or
                    and                       >
            *               -               e   3
        +       b         /   3.14
       a 1               c d
     */
    static final String bool_expression3 = "(((a + 1) * b > 0) and (c / d - 3.14 < 0)) or e > 3";
    static final String num_expression1 = "(a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6)";
    static final String num_expression2 = "(a + 3.16e-9) * ((b + 3.14E7) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6594162598152)";

    static final ElContext context1 = ElContext.createContext();
    static final ElContext context2 = ElContext.createContext();
    static final ElContext context3 = ElContext.createContext();

    @BeforeAll
    static void init() {
        context1.set("a", 3.0);
        context1.set("b", 1.414);
        context1.set("c", 6.32);

        context2.set("a", 3.0);
        context2.set("b", 0.618);
        context2.set("c", 5.0);

        context3.set("a", "N");
        context3.set("b", 9.11);
        context3.set("c", new String[]{"x", "y", "z"});
    }

    @Test
    void testBool1() {
        evaluate(bool_expression1, context1, false);
        evaluate(bool_expression1, context2, true);
    }

    @Test
    void testBool2() {
        evaluate(bool_expression2, context3, true);
    }

    @Test
    void testBool3() {
        evaluate(bool_expression3, true,
                "a", 2, "b", 3.14, "c", 3.14, "d", 1, "e", 4);
    }

    @Test
    void testNum1() {
        evaluate(num_expression1, context1, -12.654354668039371);
        evaluate(num_expression1, context2, -29.620944065327453);
    }

    @Test
    void testNum2() {
        evaluate(num_expression2, context1, -19782453309790.62);
        evaluate(num_expression2, context2, -19782453309798.887);
    }
}
