package com.gether.bigdata.jvm.java8;


import com.gether.bigdata.forkjoin.SelectMaxProblemTask;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by myp on 2017/7/19.
 */
public class ForkjoinTest {

    @Test
    public void getMaxNumbers() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        int size = 110000000;
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i;
        }
        int nThreads = 8;
        SelectMaxProblemTask selectMaxProblemTask = new SelectMaxProblemTask(0, size, numbers);
        ForkJoinPool fjPool = new ForkJoinPool(nThreads);
        //fjPool.invoke(selectMaxProblemTask);
        ForkJoinTask<Integer> result = fjPool.submit(selectMaxProblemTask);
        while (!selectMaxProblemTask.isDone()) {
            showLog(fjPool);
            Thread.sleep(10);
        }

        System.out.println(result.get());
        System.out.println(System.currentTimeMillis() - start);
    }

    /**
     * getPoolSize(): 此方法返回 int 值，它是ForkJoinPool内部线程池的worker线程们的数量。
     * getParallelism(): 此方法返回池的并行的级别。
     * getActiveThreadCount(): 此方法返回当前执行任务的线程的数量。
     * getRunningThreadCount():此方法返回没有被任何同步机制阻塞的正在工作的线程。
     * getQueuedSubmissionCount(): 此方法返回已经提交给池还没有开始他们的执行的任务数。
     * getQueuedTaskCount(): 此方法返回已经提交给池已经开始他们的执行的任务数。
     * hasQueuedSubmissions(): 此方法返回 Boolean 值，表明这个池是否有queued任务还没有开始他们的执行。
     * getStealCount(): 此方法返回 long 值，worker 线程已经从另一个线程偷取到的时间数。
     * isTerminated(): 此方法返回 Boolean 值，表明 fork/join 池是否已经完成执行。
     *
     * @param pool
     */
    private static void showLog(ForkJoinPool pool) {
        System.out.printf("**********************\n");
        System.out.printf("Main: Fork/Join Pool log\n");
        System.out.printf("Main: Fork/Join Pool: Parallelism:%d\n",
                pool.getParallelism());
        System.out.printf("Main: Fork/Join Pool: Pool Size:%d\n",
                pool.getPoolSize());
        System.out.printf("Main: Fork/Join Pool: Active Thread Count:%d\n",
                pool.getActiveThreadCount());
        System.out.printf("Main: Fork/Join Pool: Running Thread Count:%d\n",
                pool.getRunningThreadCount());
        System.out.printf("Main: Fork/Join Pool: Queued Submission:%d\n",
                pool.getQueuedSubmissionCount());
        System.out.printf("Main: Fork/Join Pool: Queued Tasks:%d\n",
                pool.getQueuedTaskCount());
        System.out.printf("Main: Fork/Join Pool: Queued Submissions:%s\n",
                pool.hasQueuedSubmissions());
        System.out.printf("Main: Fork/Join Pool: Steal Count:%d\n",
                pool.getStealCount());
        System.out.printf("Main: Fork/Join Pool: Terminated :%s\n",
                pool.isTerminated());
        System.out.printf("**********************\n");
    }


    @Test
    public void testNormalAdd() {
        long start = System.currentTimeMillis();
        long sum = 0L;
        for (int i = 0; i < 147483647; i++) {
            sum += i;
        }
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(sum);
    }

    @Test
    public void testJoin() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t = new Thread(() -> {
            try {
                System.out.println("t start");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        t.join();
        System.out.println(System.currentTimeMillis() - start);
    }
}