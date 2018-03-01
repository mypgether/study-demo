package com.gether.bigdata.jvm.jdk.gc;

import org.apache.commons.collections.MultiHashMap;
import org.junit.Test;

import java.util.Map;

/**
 * Created by myp on 2017/3/24.
 */
public class JVMGCTest {

    private Map<String, Object> map = new MultiHashMap();


    @Test
    public void allocateMem() throws InterruptedException {
        //for (int i = 0; i < 100000; i++) {
        //
        //}
        Thread.sleep(1000 * 5);
        while (true) {
            byte[] bytes = new byte[200 * 1024];
            map.put("0", bytes);
            int len = bytes.length;
            Thread.sleep(100);
        }
    }
}