package com.gether.bigdata.jvm.jdk;

/**
 * Created by myp on 2017/3/20.
 */
public class JVMHprofTest {
    public void slowMethod() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void slowerMethod() {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JVMHprofTest test = new JVMHprofTest();
        test.slowerMethod();
        test.slowMethod();
    }
}