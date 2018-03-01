package com.gether.bigdata.jvm.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Testing how slow is throwing an exception in different scenarios
 * Benchmark                                  Mode  Cnt     Score     Error   Units
 ExceptionTests.createNewNoFillStackTrace  thrpt   10    38.087 ±   2.463  ops/us
 ExceptionTests.createNewNormalException   thrpt   10     0.712 ±   0.014  ops/us
 ExceptionTests.createNewStringHolder      thrpt   10   195.814 ±  13.717  ops/us
 ExceptionTests.throwCached                thrpt   10  1747.492 ± 127.040  ops/us
 ExceptionTests.throwNew                   thrpt   10     0.693 ±   0.078  ops/us
 ExceptionTests.throwNewNoFillStackTrace   thrpt   10    46.738 ±   2.479  ops/us
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS )
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS )
@Threads(1)
@State(Scope.Thread)
public class ExceptionTests {
    private static final String SOMETHING_BAD_HAS_HAPPENED = "Something bad has happened";
    private MyOwnException m_cached;
    private String m_str;

    @Setup(Level.Iteration)
    public void setup() {
        m_cached = new MyOwnException(SOMETHING_BAD_HAS_HAPPENED);
        final byte[] bytes = new byte[100];
        new Random().nextBytes(bytes);
        m_str = new String(bytes, StandardCharsets.ISO_8859_1);
    }

    @Benchmark
    public void throwCached() {
        try {
            throw m_cached;
        } catch (MyOwnException ignored) {
        }
    }

    @Benchmark
    public void throwNew() {
        try {
            throw new MyOwnException(SOMETHING_BAD_HAS_HAPPENED);
        } catch (MyOwnException ignored) {
        }
    }

    @Benchmark
    public void throwNewNoFillStackTrace() {
        try {
            throw new NoStackTraceException(SOMETHING_BAD_HAS_HAPPENED);
        } catch (NoStackTraceException ignored) {
        }
    }

    @Benchmark
    public NoStackTraceException createNewNoFillStackTrace() {
        return new NoStackTraceException(SOMETHING_BAD_HAS_HAPPENED);
    }

    @Benchmark
    public MyOwnException createNewNormalException() {
        return new MyOwnException(SOMETHING_BAD_HAS_HAPPENED);
    }

    @Benchmark
    public StringHolder createNewStringHolder() {
        return new StringHolder(m_str);
    }

    private static class MyOwnException extends Exception {
        public MyOwnException(String message) {
            super(message);
        }
    }

    private static class NoStackTraceException extends Exception {
        public NoStackTraceException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    private static class StringHolder {
        private final String m_str;

        private StringHolder(String str) {
            this.m_str = str;
        }

        public String getStr() {
            return m_str;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + ExceptionTests.class.getSimpleName() + ".*")
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
