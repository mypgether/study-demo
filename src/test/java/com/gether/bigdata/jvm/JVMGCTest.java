package com.gether.bigdata.jvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 针对threadlocal会不会导致内存溢出的测试，其实线程结束后，threadlocal也就结束了。所以不会导致内存溢出。
 * 但是如果线程卡死，sleep，那么就会导致内存一直增高
 */
public class JVMGCTest {

    public static void main(String[] args) throws Exception {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int testCase= Integer.parseInt(br.readLine());
        br.close();
        
        switch(testCase){
            // 测试情况1. 无线程池，线程不休眠，并且清除thread_local 里面的线程变量；测试结果：无内存溢出
            case 1 :testWithThread(true, 0); break;
            // 测试情况2. 无线程池，线程不休眠，没有清除thread_local 里面的线程变量；测试结果：无内存溢出
            case 2 :testWithThread(false, 0); break;
            // 测试情况3. 无线程池，线程休眠1000毫秒,清除thread_local里面的线程的线程变量；测试结果：无内存溢出,但是新生代内存整体使用高
            case 3 :testWithThread(false, 1000); break;
            // 测试情况4. 无线程池，线程永久休眠（设置最大值）,清除thread_local里面的线程的线程变量；测试结果：内存溢出
            case 4 :testWithThread(true, Integer.MAX_VALUE); break;
            // 测试情况5. 有线程池，线程池大小50，线程不休眠，并且清除thread_local 里面的线程变量；测试结果：无内存溢出
            case 5 :testWithThreadPool(50,true,0); break;
            // 测试情况6. 有线程池，线程池大小50，线程不休眠，没有清除thread_local 里面的线程变量；测试结果：无内存溢出
            case 6 :testWithThreadPool(50,false,0); break;
            // 测试情况7. 有线程池，线程池大小50，线程无限休眠，并且清除thread_local 里面的线程变量；测试结果：无内存溢出
            case 7 :testWithThreadPool(50,true,Integer.MAX_VALUE); break;
            // 测试情况8. 有线程池，线程池大小1000，线程无限休眠，并且清除thread_local 里面的线程变量；测试结果：无内存溢出
            case 8 :testWithThreadPool(1000,true,Integer.MAX_VALUE); break;
            
            default :break;
        
        }        
    }

    public static void testWithThread(boolean clearThreadLocal, long sleepTime) {
        System.out.println("testWithThread");
        while (true) {
            try {
                Thread.sleep(100);
                new Thread(new TestTask(clearThreadLocal, sleepTime)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testWithThreadPool(int poolSize,boolean clearThreadLocal, long sleepTime) {
        System.out.println("testWithThreadPool");
        ExecutorService service = Executors.newFixedThreadPool(poolSize);
        while (true) {
            try {
                Thread.sleep(100);
                service.execute(new TestTask(clearThreadLocal, sleepTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final byte[] allocateMem() {
        // 这里分配一个1M的对象
        byte[] b = new byte[1024 * 1024];
        return b;
    }

    static class TestTask implements Runnable {

        /** 是否清除上下文参数变量 */
        private boolean clearThreadLocal;
        /** 线程休眠时间 */
        private long sleepTime;

        public TestTask(boolean clearThreadLocal, long sleepTime) {
            this.clearThreadLocal = clearThreadLocal;
            this.sleepTime = sleepTime;
        }

        public void run() {
            try {
                ThreadLocalHolder.set(allocateMem());
                try {
                    // 大于0的时候才休眠，否则不休眠
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException e) {

                }
            } finally {
                if (clearThreadLocal) {
                    ThreadLocalHolder.clear();
                }
            }
        }
    }

}