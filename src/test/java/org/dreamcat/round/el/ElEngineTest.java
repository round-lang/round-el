package org.dreamcat.round.el;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.dreamcat.common.io.ClassPathUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
@Slf4j
class ElEngineTest {

    private ElEngine createElEngine() {
        ElEngine engine = ElEngine.getEngine();
        // extend function
        engine.getSettings().setEnableExtendedFunction(true);
        engine.setExtendedFunction(Integer.class, "e", (obj, args) ->
                Math.E * ((Number) obj).intValue());
        engine.setExtendedFunction(Double.class, "pow3", (obj, args) ->
                Math.pow(((Number) obj).doubleValue(), 3.0));
        // custom function
        engine.setFunction("printf", args -> {
            System.out.printf((String) args[0], (Object[])
                    Arrays.copyOfRange(args, 1, args.length));
            return null;
        });
        engine.setFunction("println", args -> {
            System.out.println(args[0]);
            return null;
        });
        engine.setFunction("date_str_to_number", args -> {
            try {
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                        .parse((String) args[0]).getTime();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        return engine;
    }

    @Test
    void test() throws IOException {
        ElEngine engine = createElEngine();
        String expression = ClassPathUtil.getResourceAsString("el.txt");
        // compile
        ElString elString = engine.compile(expression);
        // runtime
        ElContext context = ElContext.of();
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

}
