package org.dreamcat.round.el.bench;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
public class NumElBenchmarkCompiled extends NumElBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(NumElBenchmarkCompiled.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    private static final boolean is_compiled = true;

    public static class Normal extends NumElBenchmarkBase {

        public Normal() {
            super(expression, context, is_compiled);
        }
    }

    public static class LinerHuge10 extends NumElBenchmarkBase {

        public LinerHuge10() {
            super(mixUpLiner(10), context, is_compiled);
        }
    }

    public static class LinerHuge100 extends NumElBenchmarkBase {

        public LinerHuge100() {
            super(mixUpLiner(100), context, is_compiled);
        }
    }

    public static class LinerHuge1000 extends NumElBenchmarkBase {

        public LinerHuge1000() {
            super(mixUpLiner(1000), context, is_compiled);
        }
    }
}
