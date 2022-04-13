package org.dreamcat.round.el.comparison.speed;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.internal.Engine;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BoolElSpeedBenchmark {

    static final String expression = "a == 3 && ((b + 3.14) > 5 || (c - a * 2) / 3 <= 0)";

    static final JexlEngine jexlEngine = new Engine();
    static final JexlContext jexlContext = new MapContext();

    static final Map<String, Object> ognlContext = new HashMap<>();

    static final ExpressRunner expressRunner = new ExpressRunner();
    static final DefaultContext<String, Object> qlExpressContext = new DefaultContext<>();

    @Benchmark
    @Fork(value = 2)
    @Measurement(iterations = 10, time = 1)
    @Warmup(iterations = 5, time = 1)
    public Object jexl() {
        return jexlEngine.createExpression(expression).evaluate(jexlContext);
    }

    @Benchmark
    @Fork(value = 2)
    @Measurement(iterations = 10, time = 1)
    @Warmup(iterations = 5, time = 1)
    public Object ognl() throws OgnlException {
        return Ognl.getValue(expression, null, ognlContext);
    }

    @Benchmark
    @Fork(value = 2)
    @Measurement(iterations = 10, time = 1)
    @Warmup(iterations = 5, time = 1)
    public Object qlExpress() throws Exception {
        return expressRunner.execute(expression, qlExpressContext,
                Collections.emptyList(), false, false);
    }

    static {
        jexlContext.set("a", 3);
        jexlContext.set("b", 1.414);
        jexlContext.set("c", 6.32);

        ognlContext.put("a", 3);
        ognlContext.put("b", 1.414);
        ognlContext.put("c", 6.32);

        qlExpressContext.put("a", 3);
        qlExpressContext.put("b", 1.414);
        qlExpressContext.put("c", 6.32);
    }
}
