package org.dreamcat.round.el.comparison.jmh;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

/**
 * @author Jerry Will
 * @since 2021-07-07
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HelloWorldBenchmark {

    @Benchmark
    public int helloWorldBenchmark() {
        return 1;
    }

    @Benchmark
    @Measurement(iterations = 1, time = 1)
    public void milli1() throws InterruptedException {
        Thread.sleep(1);
    }

    @Benchmark
    @Measurement(iterations = 1, time = 2)
    public void milli2() throws InterruptedException {
        Thread.sleep(1);
    }
}
