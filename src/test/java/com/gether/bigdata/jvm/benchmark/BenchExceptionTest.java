package com.gether.bigdata.jvm.benchmark;

import com.gether.bigdata.jvm.clazz.ErrorException;
import com.gether.bigdata.jvm.clazz.NoStack2Exception;
import com.gether.bigdata.jvm.clazz.NoStackException;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by myp on 2017/3/4.
 *
 Run complete. Total time: 00:01:03
 Benchmark                          Mode  Cnt    Score     Error  Units
 ThrowableTest.throwErrorNoStack    avgt    5   11.731 ±  61.304  ms/op
 ThrowableTest.throwErrorNoStack2   avgt    5   58.570 ± 139.476  ms/op
 ThrowableTest.throwErrorNoStack3   avgt    5   11.265 ±  52.131  ms/op
 ThrowableTest.throwErrorWithStack  avgt    5  117.114 ± 597.491  ms/op
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchExceptionTest {

    List<Object> lists;

    @Setup
    public void before() {
        lists = Lists.newArrayList();
    }

    @Benchmark
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    public void throwErrorWithStack() {
        for (int i = 0; i < 10000; i++) {
            lists.add(new ErrorException("message"));
        }
    }

    @Benchmark
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    public void throwErrorNoStack() {
        for (int i = 0; i < 10000; i++) {
            lists.add(new ErrorException("message", null, false, false));
        }
    }

    @Benchmark
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    public void throwErrorNoStack2() {
        for (int i = 0; i < 10000; i++) {
            lists.add(new NoStackException("message"));
        }
    }

    @Benchmark
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    public void throwErrorNoStack3() {
        for (int i = 0; i < 10000; i++) {
            lists.add(new NoStack2Exception("message"));
        }
    }

    @Test
    public void testthrowErrorNoStack() {
        try {
            throw new ErrorException("message", null, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testthrowErrorNoStack3() {
        try {
            throw new NoStack2Exception("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchExceptionTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(5)
                .build();

        new Runner(opt).run();
    }
}