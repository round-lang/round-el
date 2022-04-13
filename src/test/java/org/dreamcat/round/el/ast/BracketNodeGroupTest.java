package org.dreamcat.round.el.ast;

import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-08-07
 */
class BracketNodeGroupTest extends NodeTest {

    @Test
    void testList() {
        analyseAndShow("[]");
        analyseAndShow("[true, x.y.z, y.z(x, [['a',[]],[[],(b)]])].has(A.sin(x))");
    }
}
