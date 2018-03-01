package com.gether.bigdata.jvm.benchmark;

import com.google.common.collect.Lists;
import org.openjdk.jmh.annotations.*;
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
 Benchmark                                             Mode  Cnt    Score     Error  Units
 BenchBLRandomAccessListTest.measureListIntBL          avgt   10   56.954 ±  18.999  ms/op
 BenchBLRandomAccessListTest.measureListIteratorBL     avgt   10   60.405 ±   5.318  ms/op
 BenchBLRandomAccessListTest.measureListIteratorObjBL  avgt   10   64.974 ±  22.062  ms/op
 BenchBLRandomAccessListTest.measureList增强ForBL        avgt   10  105.512 ± 130.696  ms/op

 Benchmark                                              Mode  Cnt   Score   Error  Units
 BenchBLRandomAccessListTest.measureListIntBL          thrpt   10  17.266 ± 6.103  ops/s
 BenchBLRandomAccessListTest.measureListIteratorBL     thrpt   10  16.272 ± 2.435  ops/s
 BenchBLRandomAccessListTest.measureListIteratorObjBL  thrpt   10  14.075 ± 2.296  ops/s
 BenchBLRandomAccessListTest.measureList增强ForBL        thrpt   10  16.701 ± 2.980  ops/s
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class BenchBLRandomAccessListTest {

    int TIMES = 10000;


    List<String> lists;

    @Setup
    public void before() {
        lists = Lists.newArrayList();
        for (int i = 0; i < 10000; i++) {
            lists.add("1");
        }
    }

    @Benchmark
    public void measureList增强ForBL() {
        for (int j = 0; j < TIMES; j++) {
            for (String list : lists) {
            }
        }
    }

    @Benchmark
    public void measureListIteratorBL() {
        for (int j = 0; j < TIMES; j++) {
            for (Iterator i = lists.iterator(); i.hasNext(); ) {
                String temp = (String) i.next();
            }
        }
    }

    @Benchmark
    public void measureListIteratorObjBL() {
        for (int j = 0; j < TIMES; j++) {
            Iterator i = null;
            for (i = lists.iterator(); i.hasNext(); ) {
                String temp = (String) i.next();
            }
        }
    }

    @Benchmark
    public void measureListIntBL() {
        for (int j = 0; j < TIMES; j++) {
            int length = lists.size();
            for (int i = 0; i < length; i++) {
                String temp = lists.get(i);
            }
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchBLRandomAccessListTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(10)
                .build();
        new Runner(opt).run();
    }
}