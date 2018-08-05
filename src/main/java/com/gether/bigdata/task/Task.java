package com.gether.bigdata.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Future;

@Component
public class Task {

  public static Random random = new Random();

  @Async
  public Future<Boolean> doTaskOne() throws Exception {
    System.out.println("开始做任务一");
    long start = System.currentTimeMillis();
    Thread.sleep(random.nextInt(10000));
    long end = System.currentTimeMillis();
    System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
    return new AsyncResult<>(true);
  }

  @Async
  public Future<Boolean> doTaskTwo() throws Exception {
    System.out.println("开始做任务二");
    long start = System.currentTimeMillis();
    Thread.sleep(random.nextInt(10000));
    long end = System.currentTimeMillis();
    System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");
    return new AsyncResult<>(true);
  }

  @Async
  public Future<Boolean> doTaskThree() throws Exception {
    System.out.println("开始做任务三");
    long start = System.currentTimeMillis();
    Thread.sleep(random.nextInt(10000));
    long end = System.currentTimeMillis();
    System.out.println("完成任务三，耗时：" + (end - start) + "毫秒");
    return new AsyncResult<>(true);
  }
}