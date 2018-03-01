package com.gether.bigdata.jvm.benchmark;

import com.gether.bigdata.util.StringBuilderHelper;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by myp on 2017/3/4.
 * # Run complete. Total time: 00:00:26

 Benchmark                                 Mode  Cnt   Score   Error  Units
 BenchStringBufferTest.benchString         avgt    6   9.059 ± 1.550  ns/op
 BenchStringBufferTest.benchStringBuffer   avgt    6  19.048 ± 3.015  ns/op
 BenchStringBufferTest.benchStringBuilder  avgt    6  19.223 ± 1.203  ns/op
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)

//@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.MICROSECONDS)
//@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS )
//@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS )
//@Threads(1)
//@State(Scope.Thread)
public class BenchStringBufferTest {

    private static final ThreadLocal<StringBuilderHelper>
            threadLocalStringBuilderHelper = new ThreadLocal<StringBuilderHelper>() {
        @Override
        protected StringBuilderHelper initialValue() {
            return new StringBuilderHelper();
        }
    };

    /**
     * 测试stringbuffer对象的重用性
     */
    @Test
    public void test() {
        StringBuilder sb = threadLocalStringBuilderHelper.get().getStringBuilder();
        sb.append("1000");
        System.out.println(sb.toString());
        sb.append("123908219381203");
        sb.append("123908219381203");
        sb.append("123908219381203");
        sb.append("123908219381203");
        System.out.println(sb.toString());
        System.out.println("capacity" + sb.capacity());

        sb = threadLocalStringBuilderHelper.get().getStringBuilder();
        System.out.println("capacity" + sb.capacity());
        System.out.println("sb.toString:" + sb.toString());
        sb.append("123908219381203");
        System.out.println("sb.toString:" + sb.toString());
        System.out.println("end----");
    }


    @Test
    public void mathTest() {
        System.out.println(Math.ceil(10 / 0.7));
    }


    @Benchmark
    public void benchString() {
        String s = "a";
        s = s + "asdasd";
    }


    @Benchmark
    public void benchStringBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append("a");
        sb.append("asdasd");
        sb.toString();
    }

    @Benchmark
    public void benchStringBuffer() {
        StringBuffer sb = new StringBuffer();
        sb.append("a");
        sb.append("asdasd");
        sb.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchStringBufferTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(6)
                .build();
        new Runner(opt).run();
    }
}