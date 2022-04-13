package org.dreamcat.round.el;

import lombok.SneakyThrows;
import org.dreamcat.common.util.ReflectUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
class SimpleElAssignTest {

    static final ElEngine engine = ElEngine.getEngine();

    static final String expression = "x = a == 3 && (b + 3.14);\n"
            + "y = (c - a * 2) / 3 <= 0;"
            + "z = elvis(x || y, a in d[..-2] or sqrt_diff(a, b) < sin(c))";

    @SneakyThrows
    @Test
    void testBool1() {
        ElString el = engine.compile(expression);
        System.out.println(ReflectUtil.getValue(el, ReflectUtil.retrieveField(
                el.getClass(), "root")));
    }
}
