package com.gether.bigdata.jvm.benchmark;

import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by myp on 2017/3/4.
 */

/**
 # Run complete. Total time: 00:01:15

 Benchmark                                       Mode  Cnt   Score    Error  Units
 BenchBLLinkedListTest.measureListIntBL          avgt    5  50.031 ± 10.993  ms/op
 BenchBLLinkedListTest.measureListIteratorBL     avgt    5   0.025 ±  0.011  ms/op
 BenchBLLinkedListTest.measureListIteratorObjBL  avgt    5   0.032 ±  0.020  ms/op
 BenchBLLinkedListTest.measureList增强ForBL        avgt    5   0.032 ±  0.034  ms/op

 Benchmark                                        Mode  Cnt   Score    Error   Units
 BenchBLLinkedListTest.measureListIntBL          thrpt    5   0.021 ±  0.001  ops/ms
 BenchBLLinkedListTest.measureListIteratorBL     thrpt    5  39.947 ± 18.540  ops/ms
 BenchBLLinkedListTest.measureListIteratorObjBL  thrpt    5  30.883 ± 44.491  ops/ms
 BenchBLLinkedListTest.measureList增强ForBL        thrpt    5  34.888 ± 22.149  ops/ms
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class BenchBLLinkedListTest {

    int TIMES = 1;


    List<String> lists;

    @Setup
    //@Before
    public void before() {
        lists = Lists.newLinkedList();
        for (int i = 0; i < 10000; i++) {
            lists.add("1");
        }
    }

    @Benchmark
    public void measureList增强ForBL() {
        long start = System.currentTimeMillis();
        for (int j = 0; j < TIMES; j++) {
            for (String list : lists) {
            }
        }
    }

    @Benchmark
    public void measureListIteratorBL() {
        long start = System.currentTimeMillis();
        for (int j = 0; j < TIMES; j++) {
            for (Iterator i = lists.iterator(); i.hasNext(); ) {
                String temp = (String) i.next();
            }
        }
    }

    @Benchmark
    public void measureListIteratorObjBL() {
        long start = System.currentTimeMillis();
        for (int j = 0; j < TIMES; j++) {
            Iterator i = null;
            for (i = lists.iterator(); i.hasNext(); ) {
                String temp = (String) i.next();
            }
        }
    }

    @Benchmark
    //@Test
    public void measureListIntBL() {
        long start = System.currentTimeMillis();
        for (int j = 0; j < TIMES; j++) {
            int length = lists.size();
            for (int i = 0; i < length; i++) {
                String temp = lists.get(i);
            }
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchBLLinkedListTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(5)
                .build();
        new Runner(opt).run();
    }
}