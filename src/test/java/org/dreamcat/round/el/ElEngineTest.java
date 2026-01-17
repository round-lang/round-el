package org.dreamcat.round.el;

import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.util.ClassLoaderUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
@Slf4j
class ElEngineTest {

    private ElEngine createElEngine() {
        ElEngine engine = ElEngine.getTypicalEngine();
        // extend function
        engine.getSettings().enableExtendedFunction(true);
        engine.setExtendedFunction(Integer.class, "e", (obj, args) ->
                Math.E * ((Number) obj).intValue());
        engine.setExtendedFunction(Double.class, "pow3", (obj, args) ->
                Math.pow(((Number) obj).doubleValue(), 3.0));
        return engine;
    }

    @Test
    void test1() throws IOException {
        ElEngine engine = createElEngine();
        String expression = ClassLoaderUtil.getResourceAsString("el.txt");
        System.out.println(expression);
        System.out.println("---- ---- ---- ----    ---- ---- ---- ----");
        // compile
        ElString elString = engine.compile(expression);
        // runtime
        ElContext context = ElContext.create();
        context.set("s", "2021-05-25 00:00:00");
        context.set("dict", Collections.singletonMap("awe", "some"));
        context.set("log", log);
        context.set("b", 3.14);
        context.set("d", 1);
        context.set("x", "2021-05-25 00:00:00");
        context.set("y", "2718");
        context.set("z", 3.14);

        Object result = elString.evaluate(context);
        assert Objects.equals(result, true);
    }

    @Test
    void test2() throws IOException {
        ElEngine engine = createElEngine();
        String expression = ClassLoaderUtil.getResourceAsString("el_simple.txt");
        System.out.println(expression);
        System.out.println("---- ---- ---- ----    ---- ---- ---- ----");
        Object result = engine.evaluate(expression);
        System.out.println(result);
    }

}
