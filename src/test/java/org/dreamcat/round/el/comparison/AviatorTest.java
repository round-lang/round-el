package org.dreamcat.round.el.comparison;

import com.googlecode.aviator.AviatorEvaluator;
import java.util.Map;
import org.dreamcat.common.util.MapUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @version 2023-11-12
 */
public class AviatorTest {

    @Test
    void test() {
        Object res = AviatorEvaluator.execute("1.2 + 3 * 4 / 5");
        System.out.println(res);

        Map<String, Object> ctx = MapUtil.of("a", 1.2, "b", 4);
        System.out.println(AviatorEvaluator.execute("a + 3 * b / 5", ctx));
    }
}
