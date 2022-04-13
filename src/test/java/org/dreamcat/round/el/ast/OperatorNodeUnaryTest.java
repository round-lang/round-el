package org.dreamcat.round.el.ast;

import lombok.Getter;
import org.dreamcat.common.util.MapUtil;
import org.dreamcat.round.el.lex.OperatorToken;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-07-23
 */
class OperatorNodeUnaryTest extends NodeTest {

    @Test
    void test_minus1() {
        System.out.println("python3 -c 'print(-1 + + 2 - -3 * + 4)'");
        evalNode("-1 + + 2 - -3 * + 4", 13);
    }

    @Test
    void test_minus2() {
        System.out.println("python3 -c 'a,c=3.14,2;print(---a + ++1 * -c)'");
        evalNode("---a + ++1 * -b.c", engine -> {
                },
                -5.14, "a", 3.14, "b", MapUtil.of("c", 2));

        System.out.println("python3 -c 'f,h=0.33,7;print(f * h)'");
        evalNode("e.f g h", engine -> {
            engine.setFunction("g", OperatorToken.MUL);
        }, 2.31, "e", new E(0.33), "h", 7);
    }

    @Test
    void test_minus3() {
        System.out.println("python3 -c 'a,c,f,h=3.14,2,0.33,7;print(((---a + ++1 * -c) / f) * h)'");
        evalNode("---a + ++1 * -b.c d e.f g h", engine -> {
            engine.setFunction("d", OperatorToken.DIV);
            engine.setFunction("g", OperatorToken.MUL);
        }, -109.030303, "a", 3.14, "b", MapUtil.of("c", 2), "e", new E(0.33), "h", 7);
    }

    @Getter
    public static class E {

        final double f;

        E(double f) {
            this.f = f;
        }
    }

}
