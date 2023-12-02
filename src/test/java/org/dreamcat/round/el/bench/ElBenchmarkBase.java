package org.dreamcat.round.el.bench;

import static org.dreamcat.common.util.FunctionUtil.invokeOrNull;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import ognl.Node;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.internal.Engine;
import org.dreamcat.common.util.FunctionUtil;
import org.dreamcat.round.el.ElContext;
import org.dreamcat.round.el.ElEngine;
import org.dreamcat.round.el.ElString;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
@Fork(value = 2)
@Measurement(iterations = 5, time = 1)
@Warmup(iterations = 3, time = 1)
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public abstract class ElBenchmarkBase {

    private static final JexlEngine jexlEngine = new Engine();
    private static final OgnlContext ognlContextDefault = (OgnlContext) Ognl.createDefaultContext(null);
    private static final ElEngine elEngine = ElEngine.getEngine();

    protected final String expr;

    private final JexlExpression jexlExpression;
    private final JexlContext jexlContext = new MapContext();

    private final Expression aviatorExpression;
    private final Map<String, Object> aviatorEnv = new HashMap<>();

    private final Node ognlNode;
    private final Map<String, Object> ognlContext = new HashMap<>();

    private final ExpressRunner expressRunner = new ExpressRunner();
    private final DefaultContext<String, Object> qlExpressContext = new DefaultContext<>();

    private final ElString elString;
    private final ElContext elContext = ElContext.create();

    @SneakyThrows
    public ElBenchmarkBase(String expr, Map<String, Object> ctx) {
        this.expr = expr;

        ctx.forEach(jexlContext::set);
        aviatorEnv.putAll(ctx);
        ognlContext.putAll(ctx);
        qlExpressContext.putAll(ctx);
        ctx.forEach(elContext::set);

        jexlExpression = invokeOrNull(() -> jexlEngine.createExpression(expr));
        aviatorExpression = invokeOrNull(() -> AviatorEvaluator.compile(expr));
        ognlNode = invokeOrNull(() -> (Node) Ognl.parseExpression(expr));
        try {
            expressRunner.execute(expr, qlExpressContext,
                    Collections.emptyList(), true, false);
        } catch (Exception ignore) {
        }

        elString = invokeOrNull(() -> elEngine.compile(expr));
    }

    @Benchmark
    public Object jexl() {
        return jexlEngine.createExpression(expr).evaluate(jexlContext);
    }

    @Benchmark
    public Object jexlCompiled() {
        return jexlExpression.evaluate(jexlContext);
    }

    @Benchmark
    public Object aviator() {
        return AviatorEvaluator.execute(expr, aviatorEnv);
    }

    @Benchmark
    public Object aviatorCompiled() {
        return aviatorExpression.execute(aviatorEnv);
    }

    @Benchmark
    public Object ognl() throws OgnlException {
        return Ognl.getValue(expr, null, ognlContext);
    }

    @Benchmark
    public Object ognlCompiled() throws OgnlException {
        return ognlNode.getValue(ognlContextDefault, ognlContext);
    }

    @Benchmark
    public Object qlExpress() throws Exception {
        return expressRunner.execute(expr, qlExpressContext,
                Collections.emptyList(), false, false);
    }

    @Benchmark
    public Object qlExpressCompiled() throws Exception {
        return expressRunner.execute(expr, qlExpressContext,
                Collections.emptyList(), true, false);
    }

    @Benchmark
    public Object el() throws Exception {
        return elEngine.evaluate(expr, elContext);
    }

    @Benchmark
    public Object elCompiled() throws Exception {
        return elString.evaluate(elContext);
    }
}
