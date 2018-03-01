package com.gether.bigdata.jvm.jdk;

public class PidstatTest {
    public static class PidstatTask implements Runnable {
        public void run() {
            while (true) {
                double value = Math.random() * Math.random();
                //System.out.println("PidstatTask " + Thread.currentThread().getName());
            }
        }
    }

    public static class LazyTask implements Runnable {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    //System.out.println("LazyTask " + Thread.currentThread().getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new PidstatTask()).start();
        new Thread(new LazyTask()).start();
        new Thread(new LazyTask()).start();
    }
}