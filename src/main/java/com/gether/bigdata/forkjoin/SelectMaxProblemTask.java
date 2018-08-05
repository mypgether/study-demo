package com.gether.bigdata.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * Created by myp on 2017/8/15.
 */
public class SelectMaxProblemTask extends RecursiveTask<Integer> {

  private int start;
  private int end;
  private int[] numbers;

  public static final int CAPACITY = 10;

  public SelectMaxProblemTask(int start, int end, int[] numbers) {
    this.start = start;
    this.end = end;
    this.numbers = numbers;
  }

  @Override
  protected Integer compute() {
    if (end - start <= CAPACITY) {
      int max = numbers[start];
      for (int i = start; i < end; i++) {
        if (numbers[i] > max) {
          max = numbers[i];
        }
      }
      return max;
    }

    int mid = (start + end) / 2;
    SelectMaxProblemTask leftTask = new SelectMaxProblemTask(start, mid, numbers);
    SelectMaxProblemTask rightTask = new SelectMaxProblemTask(mid, end, numbers);

    leftTask.fork();
    rightTask.fork();

    Integer leftResult = leftTask.join();
    Integer rightResult = rightTask.join();
    if (leftResult > rightResult) {
      return leftResult;
    } else {
      return rightResult;
    }
  }
}
