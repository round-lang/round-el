package org.dreamcat.round.el.bench;

import java.math.BigDecimal;
import java.util.Map;
import org.dreamcat.common.eval.EvalContext;
import org.dreamcat.common.eval.EvalEngine;
import org.dreamcat.common.util.NumberUtil;
import org.openjdk.jmh.annotations.Benchmark;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
public abstract class NumElBenchmarkBase extends ElBenchmarkBase{

    private static final EvalEngine evalEngine = EvalEngine.getEngine();

    private final EvalContext evalContext = EvalContext.createContext();

    public NumElBenchmarkBase(String expr, Map<String, Object> ctx, boolean compiled) {
        super(expr, ctx, compiled);

        ctx.forEach((k ,v) -> {
            if (!(v instanceof Number)) return;
            Number n = (Number) v;
            if (n instanceof BigDecimal){
                evalContext.set(k, (BigDecimal) n);
            } else if (NumberUtil.isFloatLike(n)){
                evalContext.set(k, n.doubleValue());
            } else {
                evalContext.set(k, n.longValue());
            }
        });
    }

    @Benchmark
    public Object eval() throws Exception {
        return evalEngine.evaluate(expr, evalContext);
    }
}
