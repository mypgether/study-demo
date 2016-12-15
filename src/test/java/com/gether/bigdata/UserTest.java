package com.gether.bigdata;

import com.gether.bigdata.service.UserService;
import com.gether.bigdata.util.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author: myp
 * @date: 16/10/22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(Application.class)
public class UserTest {
    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        // 准备，清空user表
        //userService.deleteAll();
    }

    @Test
    public void test() throws Exception {
        // 插入5个用户
        //Long id1 = IdCenterImpl.getId();
        //Long id2 = IdCenterImpl.getId();
        Long id1 = System.currentTimeMillis();
        Long id2 = System.currentTimeMillis() + 1;
        userService.add(id1, "123sadasd", 1);
        //if (true) {
        //    throw new Exception("呀呀出错了");
        //}
        userService.add(id2, "name2", 2);
        userService.add(System.currentTimeMillis() + 2, "name3", 3);
        userService.add(System.currentTimeMillis() + 3, "name4", 4);
        userService.add(System.currentTimeMillis() + 4, "name5", 5);
        // 查数据库，应该有5个用户
        //Assert.assertEquals(5, userSerivce.list().size());

        System.out.println(JsonUtils.toJsonStrWithNull(userService.getById(id1)));
        System.out.println(JsonUtils.toJsonStrWithNull(userService.getById(id2)));

        userService.update(id2, "change name", 10);
        System.out.println(JsonUtils.toJsonStrWithNull(userService.getById(id2)));

        // 删除两个用户
        userService.delete(id1);
        userService.delete(id2);
        // 查数据库，应该有3个用户
        //Assert.assertEquals(3, userService.list().size());
    }

    @Test
    public void testGetUser() {
        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    userService.getById(1481712094804L);
                    System.out.println("cost times :" + (System.currentTimeMillis() - start));
                    //System.out.println(JsonUtils.toJsonStrWithNull(userService.getById(1481712094804L)));
                }
            }).start();
        }
    }
}