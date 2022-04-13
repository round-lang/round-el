package org.dreamcat.round.el.ast;

import org.dreamcat.round.el.ElContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-17
 */
class OperatorNodeUnaryEdgeTest extends NodeTest {

    static final ElContext context1 = ElContext.createContext();

    @BeforeAll
    static void init() {
        context1.set("e", 3.14);
    }

    @Test
    void testBlank() {
        evalNode("e", context1, 3.14);
        evalNode(" e", context1, 3.14);
        evalNode("  e", context1, 3.14);

        evalNode("e ", context1, 3.14);
        evalNode("e  ", context1, 3.14);
        evalNode("e\t", context1, 3.14);
        evalNode("e\b", context1, 3.14);

        evalNode("  e ", context1, 3.14);
        evalNode(" e  ", context1, 3.14);
        evalNode("  e  ", context1, 3.14);
        evalNode("\te\t", context1, 3.14);
    }

    @Test
    void testUnaryOp1() {
        evalNode("-e", context1, -3.14);
        evalNode("--e", context1, 3.14);
        evalNode("---e", context1, -3.14);
        evalNode("- -e", context1, 3.14);
        evalNode("- --e", context1, -3.14);
        evalNode("- ---e", context1, 3.14);
        evalNode("-- -e", context1, -3.14);
        evalNode("-- --e", context1, 3.14);
        evalNode("-- ---e", context1, -3.14);
        evalNode("--- -e", context1, 3.14);
        evalNode("--- --e", context1, -3.14);
        evalNode("--- ---e", context1, 3.14);

        evalNode("- e", context1, -3.14);
        evalNode("-- e", context1, 3.14);
        evalNode("--- e", context1, -3.14);
        evalNode("- - e", context1, 3.14);
        evalNode("- -- e", context1, -3.14);
        evalNode("- --- e", context1, 3.14);
        evalNode("-- - e", context1, -3.14);
        evalNode("-- -- e", context1, 3.14);
        evalNode("-- --- e", context1, -3.14);
        evalNode("--- - e", context1, 3.14);
        evalNode("--- -- e", context1, -3.14);
        evalNode("--- --- e", context1, 3.14);

        evalNode("- - --- -- e", context1, -3.14);
        evalNode("- - --- -- -e", context1, 3.14);
    }

    @Test
    void testUnaryOp2() {
        evalNode("-e + 5", context1, 1.8599);
        evalNode("--e + 5", context1, 8.14);
        evalNode("---e + 5", context1, 1.8599);
        evalNode("----e + 5", context1, 8.14);

        evalNode("-e + -5", context1, -8.14);
        evalNode("--e + --5", context1, 8.14);
        evalNode("--e + ---5", context1, -1.8599);
        evalNode("----e + ----5", context1, 8.14);

        evalNode("-e + -5 - -3", context1, -5.14);
        evalNode("--e + --5 + +1", context1, 9.14);
        evalNode("-e + -5 - --3", context1, -11.14);
        evalNode("--e + --5 + -1", context1, 7.14);
    }

    @Test
    void testUnaryOp3() {
        evalNode("-e + + -5", context1, -8.14);
        evalNode("-e + - + -5", context1, 1.8599);
        evalNode("-e + - + - + -5", context1, -8.14);
        evalNode("- + -e - + -5", context1, 8.14);
    }

    @Test
    void testUnaryOp4() {
        evalNode("-(e + -5)", context1, 1.8599);
        evalNode("-(-e + -5)", context1, 8.14);
        evalNode("-(-e + --5)", context1, -1.8599);
        evalNode("-(-e + --5) - 1", context1, -2.8599);
        evalNode("-(-e + --5) - (1)", context1, -2.8599);
    }
}
