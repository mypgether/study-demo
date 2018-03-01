package com.gether.bigdata.jvm.lock;

/**
 * Created by myp on 2017/1/24.
 */
public class DeadLockTest {

    private static Object a = new Object();
    private static Object b = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (a) {
                    try {
                        System.out.println("t1 get a lock");
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 start get lock b");
                    synchronized (b) {
                        System.out.println("t1 get b lock");
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (b) {
                    try {
                        System.out.println("t2 get b lock");
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t2 start get lock a");
                    synchronized (a) {
                        System.out.println("t2 get a lock");
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }
}