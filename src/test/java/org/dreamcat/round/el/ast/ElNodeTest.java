package org.dreamcat.round.el.ast;

import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-09
 */
class ElNodeTest extends NodeTest {

    @Test
    void testBool_1() {
        /*
                        and
                *                >=
           +       b        c               -
         a   1                       *          /
                                    c  b      d    -
                                                   e
                                                  3.14
         */
        analyseAndShow("( ((a + 1) * b) and (c >= c * b - d / -e(3.14)) )");
    }

    @Test
    void testBool_2() {
        analyseAndShow("[true, x.y.z, y.z(x, [['a',[]],[[],(b)]])].has(A.sin(x))");
    }

    @Test
    void testBool() {
        analyseAndShow(
                "( ((a + 1) * b) and (c >= c * b - d / -e(3.14)) ) || [true, x.y.z, y.z(x, [['a',[]],[[],(b)]])].has(A.sin(x))");
    }
}
