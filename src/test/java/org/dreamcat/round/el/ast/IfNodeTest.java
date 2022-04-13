package org.dreamcat.round.el.ast;

import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-09-06
 */
class IfNodeTest extends NodeTest {

    @Test
    void test1() {
        evalNode("a = 1; b = -1; i = a + b; "
                + "if(a > 3 || b < 0) i = i + 1;else i = i - 1; i", 1);
        evalNode("a = 1; b = -1; i = a + b; "
                + "if(a > 3 || b < 0) { i = i + 1; } else i = i - 1; i", 1);
        evalNode("a = 1; b = -1; i = a + b; "
                + "if(a > 3 || b < 0) i = i + 1;else { i = i - 1; } i", 1);
        evalNode("a = 1; b = -1; i = a + b; "
                + "if(a > 3 || b < 0) { i = i + 1; } else { i = i - 1; } i", 1);
    }

}
