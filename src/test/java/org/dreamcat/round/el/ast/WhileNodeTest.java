package org.dreamcat.round.el.ast;

import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-09-07
 */
class WhileNodeTest extends NodeTest {

    @Test
    void test1() {
        evalNode("a = 1; b = -1; i = a + b; "
                + "while(i < 5) i = i + 1; i", 5);
        evalNode("a = 1; b = -1; i = a + b; "
                + "while(i < 5) { i = i + 1; a = a * i; } a", 120);
        evalNode("a = 1; b = -1; i = a + b; "
                + "while((i = i + 1) < 5) { a = a * i; } a", 24);
    }

    @Test
    void test2() {
        evalNode("a = 1; b = -1; i = a + b; "
                + "while (i < 10) {\n"
                + "            if (a % 4 == 0) break;\n"
                + "            i = i + 1; a = a * i;\n"
                + "        } a", 24);
    }

    @Test
    void test3() {
        String fib = "a1 = 1;\n"
                + "        a2 = 1;\n"
                + "        a3 = 1;\n"
                + "        if (n <= 2) return 1;\n"
                + "        while ((n=n-1) > 1) {\n"
                + "            a3 = a2 + a1;\n"
                + "            a1 = a2;\n"
                + "            a2 = a3;\n"
                + "        }\n"
                + "        a3";
        for (int i = 1; i <= 16; i++) {
            System.out.println(fib(i));
            evalNode(fib, fib(i), "n", i);
        }
    }

    int fib(int n) {
        int a1 = 1;
        int a2 = 1;
        int a3 = 1;
        if (n <= 2) return 1;
        while (--n > 1) {
            a3 = a2 + a1;
            a1 = a2;
            a2 = a3;
        }
        return a3;
    }
}
