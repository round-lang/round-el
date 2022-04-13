package org.dreamcat.round.el.ast;

import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-17
 */
class OperatorNodeTest extends NodeTest {

    @Test
    void test1() {
        evalNode("1 + 2 * 3 - 4 / 5", 7);
        evalNode("1 + 2 * 3 - 4 / 5.0", 6.2);
    }

    @Test
    void test2() {
        evalNode("a + b * c - d / e", 6.2,
                "a", 1, "b", 2, "c", 3, "d", 4, "e", 5.0);
    }

    @Test
    void test3() {
        // evalNode("a = 1; a", 1);
        evalNode("a = 1 + 2 * 3 - 4 / 5.0; a", 6.2);
        evalNode("a = 1; b = 2; c = 3; d = 4.0; "
                + "e = 5; a + b * c - d / e", 6.2);
    }
}
