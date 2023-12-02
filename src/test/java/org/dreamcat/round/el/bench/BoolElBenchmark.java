package org.dreamcat.round.el.bench;

import java.util.Map;
import org.dreamcat.common.util.MapUtil;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
public class BoolElBenchmark extends ElBenchmarkBase {

    static final String expression = "a == 3 && ((b + 3.14) > 5 || (c - a * 2) / 3 <= 0)";

    static final Map<String, Object> context = MapUtil.of(
            "a", 3,
            "b", 1.414,
            "c", 6.32
    );

    public BoolElBenchmark() {
        super(expression, context);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BoolElBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
