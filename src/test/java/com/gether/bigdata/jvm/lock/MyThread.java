package com.gether.bigdata.jvm.lock;

/**
 * Created by myp on 2017/3/7.
 */
public class MyThread extends Thread {

    ThreadCondition threadCondition;

    public MyThread(ThreadCondition threadCondition) {
        this.threadCondition = threadCondition;
    }

    @Override
    public void run() {
        threadCondition.await();
    }
}
