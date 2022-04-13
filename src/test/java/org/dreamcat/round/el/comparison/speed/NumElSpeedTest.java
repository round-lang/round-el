package org.dreamcat.round.el.comparison.speed;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.internal.Engine;
import org.dreamcat.common.Timeit;
import org.dreamcat.common.eval.EvalContext;
import org.dreamcat.common.eval.EvalEngine;
import org.dreamcat.common.eval.EvalExpression;
import org.dreamcat.common.x.plot.plotly.Plotly;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.ElString;
import org.junit.jupiter.api.Test;

/**
 * @author Jerry Will
 * @since 2021-07-08
 */
class NumElSpeedTest {

    @Test
    void testAssert() throws Exception {
        System.out.println("jexl:\t" + jexlExpression.evaluate(jexlContext));
        System.out.println("ognl:\t" + Ognl.getValue(expression, null, ognlContext));
        System.out.println("qlExpress:\t" + new ExpressRunner().execute(expression, qlExpressContext,
                Collections.emptyList(), false, false));
        System.out.println("el:\t" + elEngine.evaluate(expression, elContext));
        System.out.println("eval:\t" + evalEngine.evaluate(expression, evalContext));
    }

    @Test
    void testAll() {
        String[] stack = new String[]{
                "jexl", "jexl-", "ognl", "ognl-",
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
            Plotly.plotStack(x, y, n -> n / 1000_000., stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCached() {
        String[] stack = new String[]{"jexl", "ognl", "qlExpress", "el", "eval"};
        List<Integer> x = new ArrayList<>();
        List<long[]> y = new ArrayList<>();
        for (int i = 1; i <= (1 << 11); i <<= 1) {
            long[] ts = Timeit.ofActions()
                    .addAction(() -> {
                        jexlExpression.evaluate(jexlContext);
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
            Plotly.plotStack(x, y, n -> n / 1000_000., stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUncached() {
        String[] stack = new String[]{"jexl", "ognl", "qlExpress", "el", "eval"};
        List<Integer> x = new ArrayList<>();
        List<long[]> y = new ArrayList<>();
        for (int i = 1; i <= (1 << 11); i <<= 1) {
            long[] ts = Timeit.ofActions()
                    .addAction(() -> {
                        jexlEngine.createExpression(expression).evaluate(jexlContext);
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
            Plotly.plotStack(x, y, n -> n / 1000_000., stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static final String expression =
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

    static final JexlEngine jexlEngine = new Engine();
    static final JexlExpression jexlExpression = jexlEngine.createExpression(expression);
    static final JexlContext jexlContext = new MapContext();

    static final Node node;

    static {
        try {
            node = (Node) Ognl.parseExpression(expression);
        } catch (OgnlException e) {
            throw new RuntimeException(e);
        }
    }

    static final OgnlContext ognlContextDefault = (OgnlContext) Ognl.createDefaultContext(null);
    static final Map<String, Object> ognlContext = new HashMap<>();

    static final ExpressRunner expressRunner = new ExpressRunner();
    static final DefaultContext<String, Object> qlExpressContext = new DefaultContext<>();

    static final ElEngine elEngine = ElEngine.getEngine();
    static final ElString elString = elEngine.compile(expression);
    static final ElContext elContext = ElContext.createContext();

    static final EvalEngine evalEngine = EvalEngine.getEngine();
    static final EvalExpression evalExpression = evalEngine.createExpression(expression);
    static final EvalContext evalContext = EvalContext.createContext();

    static {
        jexlContext.set("a", 3.0);
        jexlContext.set("b", 1.414);
        jexlContext.set("c", 6.32);

        ognlContext.put("a", 3.0);
        ognlContext.put("b", 1.414);
        ognlContext.put("c", 6.32);

        qlExpressContext.put("a", 3.0);
        qlExpressContext.put("b", 1.414);
        qlExpressContext.put("c", 6.32);

        elContext.set("a", 3.0);
        elContext.set("b", 1.414);
        elContext.set("c", 6.32);

        evalContext.set("a", 3.0);
        evalContext.set("b", 1.414);
        evalContext.set("c", 6.32);
    }
}
