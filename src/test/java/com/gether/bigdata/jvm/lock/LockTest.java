package com.gether.bigdata.jvm.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by myp on 2017/1/24.
 */
public class LockTest {

  public static final ReentrantLock MQ_LOCK = new ReentrantLock();
  public static final Condition QUEUE_MQ_LOCK_CONDITION = MQ_LOCK.newCondition();

  public static void main(String[] args) {

    Thread t1 = new Thread(() -> {
      try {
        System.out.println("t1 start get lock");
        MQ_LOCK.lock();
        System.out.println("t1 get lock, awaiting" + MQ_LOCK.isLocked());
        QUEUE_MQ_LOCK_CONDITION.await();
        Thread.sleep(30000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        MQ_LOCK.unlock();
        System.out.println("t1 release lock");
      }
    });

    Thread t2 = new Thread(() -> {
      try {
        Thread.sleep(3000);
        System.out.println("t2 start get lock" + MQ_LOCK.isLocked());
        MQ_LOCK.lock();
        System.out.println("t2 get lock, awaiting");
        QUEUE_MQ_LOCK_CONDITION.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        MQ_LOCK.unlock();
        System.out.println("t2 release lock");
      }
    });

    t1.start();
    t2.start();
  }
}