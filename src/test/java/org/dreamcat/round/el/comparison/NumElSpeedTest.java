package org.dreamcat.round.el.comparison;

import com.googlecode.aviator.AviatorEvaluator;
import com.ql.util.express.ExpressRunner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ognl.Ognl;
import org.dreamcat.common.Timeit;
import org.dreamcat.common.plot.plotly.Plotly;
import org.dreamcat.common.util.MapUtil;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
class NumElSpeedTest extends ElTestBase {

    @Test
    void testAssert() throws Exception {
        System.out.println("jexl:\t" + jexlExpression.evaluate(jexlContext));
        System.out.println("aviator:\t" + aviatorExpression.execute(aviatorEnv));
        System.out.println("ognl:\t" + Ognl.getValue(expression, null, ognlContext));
        System.out.println("qlExpress:\t" + new ExpressRunner().execute(expression, qlExpressContext,
                Collections.emptyList(), false, false));
        System.out.println("el:\t" + elEngine.evaluate(expression, elContext));
        System.out.println("eval:\t" + evalEngine.evaluate(expression, evalContext));
    }

    @Test
    void testAll() {
        String[] stack = new String[]{
                "jexl", "jexl-", "aviator", "aviator-", "ognl", "ognl-",
                "qlExpress", "qlExpress-", "el", "el-",
                "eval", "eval-"/*, "jvm"*/
        };
        System.out.println("    " + Arrays.stream(stack).map(s -> String.format("%12s", s))
                .collect(Collectors.joining()));
        List<Integer> x = new ArrayList<>();
        List<long[]> y = new ArrayList<>();
        for (int i = 1; i <= (1 << 11); i <<= 1) {
            long[] ts = Timeit.ofActions()
                    .addAction(() -> {
                        jexlExpression.evaluate(jexlContext);
                    })
                    .addAction(() -> {
                        jexlEngine.createExpression(expression).evaluate(jexlContext);
                    })
                    .addAction(() -> {
                        aviatorExpression.execute(aviatorEnv);
                    })
                    .addAction(() -> {
                        AviatorEvaluator.execute(expression, aviatorEnv);
                    })
                    .addAction(() -> {
                        node.getValue(ognlContextDefault, ognlContext);
                    })
                    .addAction(() -> {
                        Ognl.getValue(expression, null, ognlContext);
                    })
                    .addAction(() -> {
                        expressRunner.execute(expression, qlExpressContext,
                                Collections.emptyList(), true, false);
                    })
                    .addAction(() -> {
                        expressRunner.execute(expression, qlExpressContext,
                                Collections.emptyList(), false, false);
                    })
                    .addAction(() -> {
                        elString.evaluate(elContext);
                    })
                    .addAction(() -> {
                        elEngine.evaluate(expression, elContext);
                    })
                    .addAction(() -> {
                        evalExpression.evaluate(evalContext);
                    })
                    .addAction(() -> {
                        evalEngine.evaluate(expression, evalContext);
                    })
                    /*.addAction(() -> {
                        double a = 3, b = 1.414, c = 6.32;
                        double r = (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6);
                    })*/
                    .repeat(i).count(10).skip(2)
                    .run();
            System.out.printf("%04d%s%n", i, Timeit.formatMs(ts, 12));
            x.add(i);
            y.add(ts);
        }

        try {
            Plotly.plotAndOpenTimeit(x, y, n -> n / 1000_000., stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCached() {
        String[] stack = new String[]{"jexl", "aviator", "ognl", "qlExpress", "el", "eval"};
        List<Integer> x = new ArrayList<>();
        List<long[]> y = new ArrayList<>();
        for (int i = 1; i <= (1 << 11); i <<= 1) {
            long[] ts = Timeit.ofActions()
                    .addAction(() -> {
                        jexlExpression.evaluate(jexlContext);
                    })
                    .addAction(() -> {
                        aviatorExpression.execute(aviatorEnv);
                    })
                    .addAction(() -> {
                        node.getValue(ognlContextDefault, ognlContext);
                    })
                    .addAction(() -> {
                        expressRunner.execute(expression, qlExpressContext,
                                Collections.emptyList(), true, false);
                    })
                    .addAction(() -> {
                        elString.evaluate(elContext);
                    })
                    .addAction(() -> {
                        evalExpression.evaluate(evalContext);
                    })
                    .repeat(i).count(10).skip(2)
                    .run();
            System.out.printf("%04d%s%n", i, Timeit.formatMs(ts, 12));
            x.add(i);
            y.add(ts);
        }

        try {
            Plotly.plotAndOpenTimeit(x, y, n -> n / 1000_000., stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUncached() {
        String[] stack = new String[]{"jexl", "aviator", "ognl", "qlExpress", "el", "eval"};
        List<Integer> x = new ArrayList<>();
        List<long[]> y = new ArrayList<>();
        for (int i = 1; i <= (1 << 11); i <<= 1) {
            long[] ts = Timeit.ofActions()
                    .addAction(() -> {
                        jexlEngine.createExpression(expression).evaluate(jexlContext);
                    })
                    .addAction(() -> {
                        AviatorEvaluator.getInstance().execute(expression, aviatorEnv);
                    })
                    .addAction(() -> {
                        Ognl.getValue(expression, null, ognlContext);
                    })
                    .addAction(() -> {
                        expressRunner.execute(expression, qlExpressContext,
                                Collections.emptyList(), false, false);
                    })
                    .addAction(() -> {
                        elEngine.evaluate(expression, elContext);
                    })
                    .addAction(() -> {
                        evalEngine.evaluate(expression, evalContext);
                    })
                    .repeat(i).count(10).skip(2)
                    .run();
            System.out.printf("%04d%s%n", i, Timeit.formatMs(ts, 12));
            x.add(i);
            y.add(ts);
        }

        try {
            Plotly.plotAndOpenTimeit(x, y, n -> n / 1000_000., stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public Map<String, Object> getContext() {
        return context;
    }

    @Override
    public boolean isNum() {
        return true;
    }

    private static final String expression =
            "(a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14 "
                    + "* (a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6) / 3.14";

    private static final Map<String, Object> context = MapUtil.of(
            "a", 3,
            "b", 1.414,
            "c", 6.32
    );
}
