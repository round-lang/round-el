package org.dreamcat.round.el.bench;

import static org.dreamcat.round.el.bench.BoolElBenchmark.context;
import static org.dreamcat.round.el.bench.BoolElBenchmark.expression;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
public class BoolElBenchmarkCompiled extends ElBenchmarkBase {

    public BoolElBenchmarkCompiled() {
        super(expression, context, true);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BoolElBenchmarkCompiled.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
