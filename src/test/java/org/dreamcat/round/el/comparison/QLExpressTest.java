package org.dreamcat.round.el.comparison;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.exception.QLCompileException;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
class QLExpressTest {

    static final ExpressRunner runner = new ExpressRunner();
    static final String bool_expression = "a == 3 && ((b + 3.14) > 5 || (c - a * 2) / 3 <= 0)";
    static final String num_expression1 = "(a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6)";
    static final String num_expression2 = "(a + 3.16e-9) * ((b + 3.14E7) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6594162598152)";

    static final DefaultContext<String, Object> context0 = new DefaultContext<>();
    static final DefaultContext<String, Object> context1 = new DefaultContext<>();
    static final DefaultContext<String, Object> context2 = new DefaultContext<>();

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
    void testBool() throws Exception {
        Object r1 = runner.execute(bool_expression, context1,
                Collections.emptyList(), true, false);
        assert r1.equals(false);
        Object r2 = runner.execute(bool_expression, context2,
                Collections.emptyList(), true, false);
        assert r2.equals(true);
    }

    @Test
    void testNum1() throws Exception {
        Object r1 = runner.execute(num_expression1, context1,
                Collections.emptyList(), true, false);
        assert r1 instanceof Double;
        Object r2 = runner.execute(num_expression1, context2,
                Collections.emptyList(), true, false);
        assert r2 instanceof Double;
    }

    @Test
    void testNum2() throws Exception {
        Assertions.assertThrows(QLCompileException.class, () -> {
            runner.execute(num_expression2, context1,
                    Collections.emptyList(), true, false);
        });
    }

    @Test
    void test1() throws Exception {
        Object r1 = runner.execute(" x = 2; y = 3; x * y", context0,
                Collections.emptyList(), true, false);
        assert r1.equals(6);

        Object r2 = runner.execute(" x = 2; y = 3; Math.pow(x, y)", context0,
                Collections.emptyList(), true, false);
        assert r2.equals(8.0);
    }
}
