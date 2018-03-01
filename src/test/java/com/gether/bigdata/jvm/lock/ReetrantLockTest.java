package com.gether.bigdata.jvm.lock;

/**
 * Created by myp on 2017/1/24.
 */
public class ReetrantLockTest {


    public static void main(String[] args) throws InterruptedException {
        ThreadCondition threadCondition = new ThreadCondition();

        new MyThread(threadCondition).start();

        Thread.sleep(3000);
        threadCondition.signal();
    }
}