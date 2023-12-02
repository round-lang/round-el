package org.dreamcat.round.el.bench;

import java.util.Map;
import org.dreamcat.common.util.MapUtil;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
public class LinerNumElBenchmark {

    static final String expression =
            "(a + 3.16) * ((b + 3.14) / 2.73 + (b * c - a * 2) / 3.14 + 2 * a * b / c - 6)";
    static final Map<String, Object> context = MapUtil.of(
            "a", 3,
            "b", 1.414,
            "c", 6.32
    );

    static String mixUpLiner(int size) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < size; i++) {
            char c = (i % 5) % 2 == 0 ? '+' : '-';
            if (i > 0) {
                s.append(' ').append(c).append(' ');
            }
            s.append(expression);
        }
        return s.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LinerNumElBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }


    public static class Normal extends NumElBenchmarkBase {

        public Normal() {
            super(expression, context);
        }
    }

    public static class Liner5 extends NumElBenchmarkBase {

        public Liner5() {
            super(mixUpLiner(5), context);
        }
    }

    public static class Liner10 extends NumElBenchmarkBase {

        public Liner10() {
            super(mixUpLiner(10), context);
        }
    }

    public static class Liner30 extends NumElBenchmarkBase {

        public Liner30() {
            super(mixUpLiner(30), context);
        }
    }

    public static class Liner50 extends NumElBenchmarkBase {

        public Liner50() {
            super(mixUpLiner(50), context);
        }
    }

    static class LinerTest {

        @Test
        void test() throws Exception {
            // 669: Method code too large! at com.googlecode.aviator.asm.MethodWriter.getSize
            int[] sizes = new int[]{1, 5, 10, 30, 50, 100, 300, 500, 600, 650, 665, 666, 667, 668, 669};
            for (int size: sizes) {
                NumElBenchmarkBase benchmark = new NumElBenchmarkBase(mixUpLiner(size), context){};
                System.out.println("expr: " + benchmark.expr);
                System.out.println("size: " + size);
                System.out.println(benchmark.jexl());
                System.out.println(benchmark.ognl());
                System.out.println(benchmark.qlExpress());
                System.out.println(benchmark.el());
                System.out.println(benchmark.eval());
                System.out.println(benchmark.aviator());
                System.out.println();
            }
        }
    }
}
