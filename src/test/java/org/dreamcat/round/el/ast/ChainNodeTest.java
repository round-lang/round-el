package org.dreamcat.round.el.ast;

import java.util.Objects;
import org.dreamcat.common.util.NumberUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2021-08-22
 */
class ChainNodeTest extends NodeTest {

    @Test
    void test1() {
        evalNode("System.out", System.out);
    }

    @Test
    void test2() {
        long ts = System.currentTimeMillis();
        evalNode("System.currentTimeMillis()",
                result -> (Long) (result) >= ts);
    }

    @Test
    void test3() {
        String home = System.getenv("HOME");
        evalNode("System.getenv(\"HOME\")", home);
    }

    @Test
    void test4() {
        evalNode("System.out.println(\"Hello World!\")", Objects::isNull);
    }

    @Test
    void test5() {
        A a = new A();
        evalNode("a.a.a.a", a, "a", a);
        evalNode("a.a.a.b", 0, "a", a);
        evalNode("a.a.a.a()", a, "a", a);
        evalNode("a.a.a.b()", 2, "a", a);
    }

    @Test
    void test6() {
        A a = new A();
        evalNode("a.a()", a, "a", a);
        evalNode("a.a().a", a, "a", a);
        evalNode("a.a().a.a", a, "a", a);
        evalNode("a.a().a.a().b", 5, "a", a);
        evalNode("a.a().a.a(2).b", 8, "a", a);
    }

    @Test
    void test7() {
        String s = "1970-01-01 08:00:00";
        evalNode("import java.text.SimpleDateFormat;"
                        + "SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(s).getTime()",
                0L, "s", s);
    }

    @Test
    void test8() {
        evalNode("pow(2., 16); 2.0 ** 16", elEngine -> {
            elEngine.setFunction("pow", (Object[] args) -> {
                if (args.length == 2) {
                    Number arg1 = (Number)  args[0];
                    Number arg2 = (Number)  args[0];
                    return Math.pow(arg1.doubleValue(), arg2.doubleValue());
                } else throw new RuntimeException("no impl");
            });
        }, 65536.);
    }
}
