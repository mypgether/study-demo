package com.gether.bigdata;

import com.alibaba.fastjson.JSON;
import com.gether.bigdata.idcenter.IdCenterUtils;
import com.gether.bigdata.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: myp
 * @date: 16/10/22
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@SpringApplicationConfiguration(Application.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

  @Autowired
  private UserService userService;

  @Before
  public void setUp() {
    // 准备，清空user表
    userService.deleteAll();
  }

  @Test
  @Transactional
  public void test() throws Exception {
    // 插入5个用户
    Long id1 = IdCenterUtils.getId();
    Long id2 = IdCenterUtils.getId();
    userService.add(id1, "123sadasd", 1);
    userService.add(id2, "name92", 2);

    userService.add(IdCenterUtils.getId(), "name93", 3);
    userService.add(IdCenterUtils.getId(), "name94", 4);
    userService.add(IdCenterUtils.getId(), "name95", 5);
    // 查数据库，应该有5个用户
    //Assert.assertEquals(5, userService.list().size());

    System.out.println(JSON.toJSONString(userService.getById(id1)));
    System.out.println(JSON.toJSONString(userService.getById(id2)));

    userService.update(id2, "change name", 10);
    System.out.println(JSON.toJSONString(userService.getById(id2)));

    // 删除两个用户
    userService.delete(id1);
    userService.delete(id2);
    // 查数据库，应该有3个用户
    Assert.assertEquals(3, userService.list().size());
  }

  @Test
  public void testGetUser() {
    for (int i = 0; i < 1000; i++) {
      new Thread(() -> {
        long start = System.currentTimeMillis();
        userService.getById(1481712094804L);
        System.out.println("cost times :" + (System.currentTimeMillis() - start));
      }).start();
    }
  }
}