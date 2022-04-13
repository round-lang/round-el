package org.dreamcat.round.el.ast;

import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-17
 */
class OperatorNodePriorityTest extends NodeTest {

    @Test
    void test1() {
        evaluate("1 - 2 + 3", 2);
        evaluate("1 -  2 * 2 + 3 / 3.0 * 4 + 3", 4.0);
    }

    @Test
    void test2() {
        evaluate("---a + ++1 * -c", -5.14,
                "a", 3.14, "c", 2);
        evaluate("---a + ++1 * -c / f * h", -45.56424242424242,
                "a", 3.14, "c", 2, "f", 0.33, "h", 7);
        evaluate("((---a + ++1 * -c) / f) * h", -109.03030303030303,
                "a", 3.14, "c", 2, "f", 0.33, "h", 7);
    }
}
