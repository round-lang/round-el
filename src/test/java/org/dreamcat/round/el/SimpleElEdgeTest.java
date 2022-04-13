package org.dreamcat.round.el;

import org.dreamcat.round.el.exception.CompileException;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
class SimpleElEdgeTest extends TestBase {

    @Test
    void test1() {
        evaluate("1 + 1", 2);
        evaluate("(1 + 1", CompileException.class);
        evaluate("1 (+ 1", CompileException.class);
        evaluate("1 + (1", CompileException.class);
        evaluate("1 + 1(", CompileException.class);

        evaluate(")1 + 1", CompileException.class);
        evaluate("1 )+ 1", CompileException.class);
        evaluate("1 + )1", CompileException.class);
        evaluate("1 + 1)", CompileException.class);
    }

    @Test
    void test2() {
        evaluate("(1 + 1)", 2);
        evaluate("((1 + 1))", 2);
        evaluate("((1 + 1)", CompileException.class);
        evaluate("(1 + 1))", CompileException.class);
        evaluate("(((1 + 1)))", 2);
        evaluate("((1 + 1)))", CompileException.class);
        evaluate("(((1 + 1))", CompileException.class);

        evaluate("1 + 1 / 3", 1);
        evaluate("(1 + 1 / 3)", 1);
        evaluate("((1 + 1 / 3))", 1);
        evaluate("(1 + 1 / 3))", CompileException.class);
        evaluate("((1 + 1 / 3)", CompileException.class);
        evaluate("(((1 + 1 / 3)))", 1);
        evaluate("((1 + 1 / 3)))", CompileException.class);
        evaluate("(((1 + 1 / 3))", CompileException.class);
    }
}
