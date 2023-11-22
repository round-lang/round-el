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
public class NumElBenchmark {

    static String mixUpLiner(int size) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < size; i++) {
            char c = i % 2 == 0 ? '+' : '-';
            if (i > 0) {
                s.append(' ').append(c).append(' ');
            }
            s.append(expression);
        }
        return s.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(NumElBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    static final String expression =
            "(a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6)";
    static final Map<String, Object> context = MapUtil.of(
            "a", 3,
            "b", 1.414,
            "c", 6.32
    );
    private static final boolean is_compiled = false;


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
