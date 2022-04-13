package org.dreamcat.round.el.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-08-24
 */
@SuppressWarnings({"unchecked"})
class BracketNodeTest extends NodeTest {

    @Test
    void test1() {
        evalNode("[1]", it -> ((List<?>) it).get(0).equals(1));
        evalNode("[3.14]", it -> ((List<?>) it).get(0).equals(3.14));

        evalNode("[1, 3.14]", it -> ((List<?>) it).get(0).equals(1) &&
                ((List<?>) it).get(1).equals(3.14));
    }

    @Test
    void test2() {
        evalNode("[1;]", it -> ((List<List<?>>) it).get(0).get(0).equals(1));
        evalNode("[3.14;]", it -> ((List<List<?>>) it).get(0).get(0).equals(3.14));

        evalNode("[1, 3.14;]", it -> ((List<List<?>>) it).get(0).get(0).equals(1) &&
                ((List<List<?>>) it).get(0).get(1).equals(3.14));
        evalNode("[1; 3.14]", it -> ((List<List<?>>) it).get(0).get(0).equals(1) &&
                ((List<List<?>>) it).get(1).get(0).equals(3.14));

        evalNode("[1, 3.14; 3.14, 1]", it -> ((List<List<?>>) it).get(0).get(0).equals(1) &&
                ((List<List<?>>) it).get(0).get(1).equals(3.14) &&
                ((List<List<?>>) it).get(1).get(0).equals(3.14) &&
                ((List<List<?>>) it).get(1).get(1).equals(1));
    }

    @Test
    void test3() {
        evalNode("a[0]", 1, "a", new int[]{1});
        evalNode("a[1]", 2, "a", new Integer[]{1, 2});
        evalNode("a[-1]", 3, "a", Arrays.asList(1, 2, 3));
    }

    @Test
    void test4() {
        evalNode("a[-1][0][1] = -3", -3, "a", new int[][][]{
                new int[][]{
                        new int[]{0, 1}, new int[]{1, 0},
                },
                new int[][]{
                        new int[]{1, -1}, new int[]{1, 1},
                }
        });
        evalNode("a=[1,2,3]; a[-1] = -1; a[-1]", -1);

        List<?> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        evalNode("a[-1] = -a[-1]", -3, "a", list);
        System.out.println(list);
    }

}
