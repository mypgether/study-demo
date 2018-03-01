package com.gether.bigdata;

import com.gether.bigdata.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.Future;

/**
 * @author: myp
 * @date: 16/10/22
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TaskTest {
    @Autowired
    private Task task;

    @Test
    public void test() throws Exception {
        System.out.println();
        System.out.println();
        System.out.println();
        long start = System.currentTimeMillis();
        Future<Boolean> result1 = task.doTaskOne();
        System.out.println(result1.get());
        Future<Boolean> result2 = task.doTaskTwo();
        System.out.println(result2.get());
        Future<Boolean> result3 = task.doTaskThree();
        System.err.println("total times:" + (System.currentTimeMillis() - start) + "毫秒");
    }
}