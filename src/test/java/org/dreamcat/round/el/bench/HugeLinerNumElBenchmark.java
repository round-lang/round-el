package org.dreamcat.round.el.bench;

import static org.dreamcat.round.el.bench.LinerNumElBenchmark.context;
import static org.dreamcat.round.el.bench.LinerNumElBenchmark.mixUpLiner;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jerry Will
 * @version 2023-11-25
 */
public class HugeLinerNumElBenchmark {

    public static class LinerHuge100 extends NumElBenchmarkBase {

        public LinerHuge100() {
            super(mixUpLiner(100), context);
        }
    }

    public static class LinerHuge200 extends NumElBenchmarkBase {

        public LinerHuge200() {
            super(mixUpLiner(200), context);
        }
    }

    public static class LinerHuge400 extends NumElBenchmarkBase {

        public LinerHuge400() {
            super(mixUpLiner(400), context);
        }
    }

    public static class LinerHuge600 extends NumElBenchmarkBase {

        public LinerHuge600() {
            super(mixUpLiner(600), context);
        }
    }

    public static class LinerHuge800 extends NumElBenchmarkBase {

        public LinerHuge800() {
            super(mixUpLiner(800), context);
        }
    }

    public static class LinerHuge1000 extends NumElBenchmarkBase {

        public LinerHuge1000() {
            super(mixUpLiner(1000), context);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HugeLinerNumElBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
