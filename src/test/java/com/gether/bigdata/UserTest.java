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

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UserTest {
    @Autowired
    private UserService userSerivce;

    @Before
    public void setUp() {
        // 准备，清空user表
        userSerivce.deleteAll();
    }

    @Test
    public void test() throws Exception {
        // 插入5个用户
        //Long id1 = IdCenterImpl.getId();
        //Long id2 = IdCenterImpl.getId();
        Long id1 = System.currentTimeMillis();
        Long id2 = System.currentTimeMillis();
        userSerivce.add(id1, "123sadasd", 1);
        //if (true) {
        //    throw new Exception("呀呀出错了");
        //}
        userSerivce.add(id2, "name2", 2);
        userSerivce.add(System.currentTimeMillis(), "name3", 3);
        userSerivce.add(System.currentTimeMillis(), "name4", 4);
        userSerivce.add(System.currentTimeMillis(), "name5", 5);
        // 查数据库，应该有5个用户
        //Assert.assertEquals(5, userSerivce.list().size());

        System.out.println(JsonUtils.toJsonStrWithNull(userSerivce.getById(id1)));
        System.out.println(JsonUtils.toJsonStrWithNull(userSerivce.getById(id2)));

        userSerivce.update(id2, "change name", 10);
        System.out.println(JsonUtils.toJsonStrWithNull(userSerivce.getById(id2)));

        // 删除两个用户
        userSerivce.delete(id1);
        userSerivce.delete(id2);
        // 查数据库，应该有3个用户
        //Assert.assertEquals(3, userSerivce.list().size());
    }
}