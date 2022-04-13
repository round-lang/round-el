package org.dreamcat.round.el.ast;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-09-07
 */
class ForNodeTest extends NodeTest {

    @Test
    void test1() {
        int sum = 0, n = 5;
        for (int i = 1; i <= n; i++) {
            sum += i * 2 - 1;
        }

        // pre;cond;post
        String el = "for (i = 1; i <= n; i=i+1) {\n"
                + "            sum = sum + i * 2 - 1;\n"
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);

        // pre;cond;
        el = "for (i = 1; i <= n;) {\n"
                + "           sum = sum + i * 2 - 1;\n i=i+1; "
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);

        // pre;;post
        el = "for (i = 1;; i=i+1) {\n"
                + "if(i > n) break;            sum = sum + i * 2 - 1;\n"
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);

        // ;cond;post
        el = "i = 1;for (; i <= n; i=i+1) {\n"
                + "            sum = sum + i * 2 - 1;\n"
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);

        // pre;
        el = "for (i = 1;;) {\n"
                + "if(i > n) break;            sum = sum + i * 2 - 1;\n i=i+1;"
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);

        // ;cond;
        el = "i = 1; for (;i <= n;) {\n"
                + "            sum = sum + i * 2 - 1;\n i=i+1;"
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);

        // ;;post
        el = "i = 1; for (;; i=i+1) {\n"
                + "if(i > n) break;            sum = sum + i * 2 - 1;\n"
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);

        // ;;
        el = "i = 1; for (;;) {\n"
                + "if(i > n) break;            sum = sum + i * 2 - 1;\n i=i+1;"
                + "        }sum";
        evalNode(el, sum, "sum", 0, "n", n);
    }

    @Test
    void test2() {
        String el = "for (i = 0; i < m; i=i+1) {\n"
                + "            for (j = 0; j < n; j=j+1) {\n"
                + "                if (i == 0 || j == 0) a[i][j] = 1;\n"
                + "                else a[i][j] = a[i - 1][j] + a[i][j - 1];\n"
                + "            }\n"
                + "        }\n"
                + "        return a";
        int m = 3, n = 4;
        System.out.println(Arrays.deepToString(pascal(m, n)));
        evalNode(el, a -> ((int[][]) a)[m - 1][n - 1] == pascal(m, n)[m - 1][n - 1],
                "m", m, "n", n, "a", new int[m][n]);
    }

    int[][] pascal(int m, int n) {
        int[][] a = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) a[i][j] = 1;
                else a[i][j] = a[i - 1][j] + a[i][j - 1];
            }
        }
        return a;
    }

}
