package com.gether.bigdata.jvm.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by myp on 2017/7/19.
 */
public class LambdaTest {

    @Test
    public void testLambda() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("t1 start");
        });
        t1.start();
        Thread.sleep(1 * 1000);
    }

    @Test
    public void testList() {
        List<String> list = Arrays.asList("1", "list1", "list213");
        list.forEach(a -> {
            System.out.println(a);
        });
        list.forEach((a) -> {
            System.out.println(a);
        });
    }

    @Test
    public void testStreamsFilter() {
        List<String> list = Arrays.asList("1", "list1", "list213", "1", "2");
        long count = list.stream().filter(a -> {
            //System.out.println("Go!");
            return a.equals("1");
        }).count();
        System.out.println(count);


        List<String> collectList = list.stream().filter(a -> a.equals("1")).collect(Collectors.toList());
        System.out.println(collectList);
    }

    @Test
    public void testStreamsMap() {
        List<String> list = Arrays.asList("aa", "bb", "cccc", "ddd", "2");
        List<Object> collectList = list.stream().map(a -> {
            if (a.equals("aa")) {
                return "success";
            }
            return "failed";
        }).collect(Collectors.toList());
        System.out.println(collectList);

        collectList = list.stream().map(String::toUpperCase).collect(Collectors.toList());
        System.out.println(collectList);
    }

    @Test
    public void testStreamsReduce() {
        List<String> list = Arrays.asList("aa", "bb", "cccc", "ddd", "2");

        String result = list.stream().reduce("", (res, now) -> {
            StringBuilder a = new StringBuilder(res);
            a.append(now);
            return a.toString();
        });
        System.out.println(result);
    }

    @Test
    public void testStreamsMap2Int() {
        List<String> list = Arrays.asList("aa", "bb", "cccc", "ddd", "2");

        IntSummaryStatistics result = list.stream().mapToInt(a -> {
            return a.length();
        }).summaryStatistics();
        System.out.println(result.getAverage());
        System.out.println(result.getMin());
        System.out.println(result.getMax());
        System.out.println(result.getSum());
        System.out.println(result.getCount());
        System.out.println(result);
    }

    @Test
    public void testRandom() {
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }
}