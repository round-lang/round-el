package org.dreamcat.round.el;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.dreamcat.common.io.ClassPathUtil;
import org.dreamcat.common.util.NumberUtil;
import org.dreamcat.common.x.jackson.JsonUtil;

/**
 * @author Jerry Will
 * @version 2021-07-23
 */
@SuppressWarnings({"all"})
public class TestBase {

    public String expr(String path) {
        try {
            return ClassPathUtil.getResourceAsString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void evaluate(String expression, Object expect, Object... input) {
        evaluate(expression, null, expect, input);
    }

    public void evaluate(String expression, ElContext context, Object expect) {
        evaluate(expression, context, expect, null);
    }

    public void evaluate(String expression, Consumer<ElEngine> configurator, Object expect, Object... input) {
        ElContext context = ElContext.createContext();
        int size = input.length;
        for (int i = 0; i < size; i += 2) {
            context.set(input[i].toString(), input[i + 1]);
        }

        evaluate(expression, context, expect, configurator);
    }

    public void evaluate(String expression, ElContext context, Object expect, Consumer<ElEngine> configurator) {
        System.out.printf("expression \t = \t %s %n", expression);

        ElEngine engine = ElEngine.getEngine();
        if (configurator != null) configurator.accept(engine);

        if (expect instanceof Class) {
            assertThrows((Class<Throwable>) expect, () -> {
                try {
                    ElString elString = engine.compile(expression);
                    elString.evaluate(context);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    throw e;
                }
            });
            return;
        }

        ElString string = engine.compile(expression);
        Object result = string.evaluate(context);
        System.out.printf("result \t = \t %s %n", result);
        showEval(expression, context);
        System.out.println();

        if (result instanceof Number) {
            assert NumberUtil.almostEq(((Number) result).doubleValue(),
                    ((Number) expect).doubleValue(), 6);
        } else {
            assert Objects.equals(result, expect);
        }
    }

    public void showEval(String expression, ElContext context) {
        Set<String> keys = context.names();
        String values = context.names().stream().map(context::get)
                .map(JsonUtil::toJson)
                .collect(Collectors.joining(","));
        String code = expression.replaceAll("&&", "and")
                .replaceAll("\\|\\|", "or")
                .replaceAll("'", "\"");
        if (keys.isEmpty()) {
            System.out.printf("python3 -c 'print(%s)'\n", code);
        } else {
            System.out.printf("python3 -c '%s=%s; print(%s)'\n",
                    String.join(",", keys), values, code);
        }
    }
}
