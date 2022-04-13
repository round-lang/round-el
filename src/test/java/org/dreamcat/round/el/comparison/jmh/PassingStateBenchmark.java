package org.dreamcat.round.el.comparison.jmh;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class PassingStateBenchmark {

    @State(Scope.Thread)
    public static class MyState {

        String a, b;

        @Setup(Level.Iteration)
        public void setup() {
            a = "some-val";
            b = "some-val2";
        }

        @TearDown(Level.Iteration)
        public void teardown() {
            a = b = "";
        }
    }

    @Benchmark
    public void benchmark(MyState myState) {
        //do whatever with myState
        String res = myState.a + myState.b;
        //....
    }
}
