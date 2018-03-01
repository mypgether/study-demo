package com.gether.bigdata.jvm.java8;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by myp on 2017/7/20.
 */
public class ThreadLockTest {

    public static final ReentrantLock LOCK = new ReentrantLock();
    public static final Condition LOCK_C1 = LOCK.newCondition();

    @Test
    public void testCondition() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        Thread a = new Thread(() -> {
            while (true) {
                try {
                    LOCK.lock();
                    count.incrementAndGet();
                    if (count.get() == 10) {
                        System.out.println("Thread a start signalAll");
                        //LOCK_C1.signalAll();
                        count.set(0);
                    }
                } finally {
                    //System.out.println("Thread a unlock");
                    LOCK.unlock();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        Thread b = new Thread(() -> {
            while (true) {
                try {
                    LOCK.lock();
                    System.out.println("Thread b start await");
                    LOCK_C1.await();
                    System.out.println("Thread b get lock");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Thread b unlock");
                    LOCK.unlock();
                }
            }
        });
        b.start();
        Thread.sleep(2000);
        a.start();

        a.join();
        b.join();
    }

    @Test
    public void testSynchronized() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        Object obj = new Object();
        Thread a = new Thread(() -> {
            while (true) {
                try {
                    synchronized (obj) {
                        count.incrementAndGet();
                        //System.out.println(count.get());
                        if (count.get() == 10) {
                            System.out.println("Thread a start notify");
                            obj.notifyAll();
                            count.set(0);
                        }
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });


        Thread b = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Thread b start synchronized");
                    synchronized (obj) {
                        obj.wait();
                        System.out.println("get lock");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
        b.start();
        a.start();

        a.join();
        b.join();
    }


    @Test
    public void testReentrantLock() throws InterruptedException {
        Thread a = new Thread(() -> {
            while (true) {
                try {
                    LOCK.lock();
                    System.out.println("Thread a get lock");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            }
        });


        Thread b = new Thread(() -> {
            while (true) {
                try {
                    LOCK.lock();
                    System.out.println("Thread b get lock");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Thread b unlock");
                    LOCK.unlock();
                }
            }
        });
        b.start();
        a.start();

        a.join();
        b.join();
    }

    @Test
    public void testThreadStartBtrace() throws InterruptedException {
        for (int i = 0; ; i++) {
            int finalI = i;
            new Thread(() -> System.out.println("thread start i:" + finalI)).start();
            Thread.sleep(3000);
        }
    }
}