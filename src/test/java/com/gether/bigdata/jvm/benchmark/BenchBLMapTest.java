package com.gether.bigdata.jvm.benchmark;

import com.google.common.collect.Maps;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by myp on 2017/3/4.
 */

/**
 # Run complete. Total time: 00:02:02

 Benchmark                     Mode  Cnt    Score     Error  Units
 BenchBLMapTest.measureMapBL1  avgt   10  296.448 ±  41.475  ms/op
 BenchBLMapTest.measureMapBL2  avgt   10  292.121 ±  77.842  ms/op
 BenchBLMapTest.measureMapBL3  avgt   10  710.033 ± 513.699  ms/op
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchBLMapTest {

    int TIMES = 100000000;


    Map<String, String> params = null;

    @Setup
    public void before() {
        params = Maps.newHashMap();
        for (int i = 0; i < TIMES; i++) {
            params.put(String.valueOf(i), " 213123123123");
        }
    }

    @Benchmark
    public void measureMapBL1() {
        for (String key : params.keySet()) {
            String value = params.get(key);
        }
    }

    @Benchmark
    public void measureMapBL2() {
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
        }
    }

    @Benchmark
    public void measureMapBL3() {
        Iterator iter = params.entrySet().iterator();
        Map.Entry<String, String> entry = null;
        while (iter.hasNext()) {
            entry = (Map.Entry<String, String>) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchBLMapTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(10)
                .build();
        new Runner(opt).run();
    }
}