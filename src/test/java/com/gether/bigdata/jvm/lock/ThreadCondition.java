package com.gether.bigdata.jvm.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by myp on 2017/3/7.
 */
public class ThreadCondition {

    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void await() {
        try {
            lock.lock();
            long start = System.currentTimeMillis();
            System.out.println("await start");
            condition = lock.newCondition();
            condition.await();
            System.out.println("await end, times:" + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        try {
            lock.lock();
            //condition = lock.newCondition();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}