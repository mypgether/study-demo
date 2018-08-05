package com.gether.research.jvm.producer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by myp on 2017/8/13.
 */
public class ShareObjService {

  public static final int queueSize = 10;

  private Lock lock = new ReentrantLock();

  private Condition QUEUE_EMPTY = lock.newCondition();
  private Condition QUEUE_FULL = lock.newCondition();

  private BlockingQueue queue = new LinkedBlockingQueue(queueSize);

  AtomicInteger index = new AtomicInteger(0);

  public void produce() {
    while (true) {
      lock.lock();
      try {
        if (queue.size() == queueSize) {
          System.out.println("queue is full, full start signal");
          QUEUE_FULL.await();
        }
        queue.offer(index.incrementAndGet());
        System.out.println("producer produce one");
        QUEUE_EMPTY.signal();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

  public void consumer() {
    while (true) {
      lock.lock();
      try {
        if (queue.size() == 0) {
          System.out.println("queue is empty, empty start signal");
          QUEUE_EMPTY.await();
        }
        QUEUE_FULL.signal();
        Object obj = queue.poll();
        System.out.println(" consumer this is " + obj);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }
}