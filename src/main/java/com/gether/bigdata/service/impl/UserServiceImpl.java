package com.gether.bigdata.service.impl;

import com.gether.bigdata.dao.mapper.UserMapper;
import com.gether.bigdata.domain.user.User;
import com.gether.bigdata.redis.JedisService;
import com.gether.bigdata.service.UserService;
import com.gether.bigdata.util.IdCenterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author: myp
 * @date: 16/10/22
 */
@Service("userService")
public class UserServiceImpl implements  UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JedisService jedisService;

    private static final boolean jdbcMode = false;

    public static int i = 0;

    @Autowired
    private UserService userService;

    @Override
    public void add(Long id, String name, Integer age) {
        if (jdbcMode) {
            jdbcTemplate.update("insert into T_USER(id,NAME, AGE) values(?, ?, ?)", id, name, age);
        } else {
            userMapper.insert(id, name, age);
            i = i + 1;
            if (i == 1) {
                userService.add(IdCenterUtils.getId(), "name2", 2);
                userService.add(IdCenterUtils.getId(), "name3", 3);
                userService.add(IdCenterUtils.getId(), "name4", 4);
                userService.add(IdCenterUtils.getId(), "name5", 5);
            }

            if (i == 3) {
                throw new RuntimeException("error");
            }
        }
    }

    @Override
    public User getById(Long id) {
        Object result = jedisService.getObject(String.valueOf(id));
        if (result == null) {
            String disLockKey = "distributelock" + String.valueOf(id);
            try {
                // 高并发环境下，会导致冲击db的压力，加入分布式锁即可。对锁设置超时时间，防止意外
                boolean lock = jedisService.setnx(disLockKey, "lock", 10);
                if (lock) {
                    System.err.println("get lock ...");
                    result = getFromBD(id);
                    if(result!=null) {
                        jedisService.setObject(String.valueOf(id), result, 100);
                    }
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                    getById(id);
                }
            } finally {
                // 记得需要释放锁
                jedisService.delete(disLockKey);
            }
        }
        return (User) result;
    }

    private User getFromBD(Long id) {
        System.err.println("get data from db");
        if (jdbcMode) {
            return jdbcTemplate.queryForObject("select * from T_USER where id=?", new Object[]{id}, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet resultSet, int i) throws SQLException {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setAge(resultSet.getInt("age"));
                    return user;
                }
            });
        }
        return userMapper.getById(id);
    }

    @Override
    public List<User> list() {
        if (jdbcMode) {
            return jdbcTemplate.query("select id,name,age from T_USER", new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet resultSet, int i) throws SQLException {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setAge(resultSet.getInt("age"));
                    return user;
                }
            });
        }
        return userMapper.getAll();
    }

    @Override
    public void update(Long id, String name, Integer age) {
        if (jdbcMode) {
            jdbcTemplate.update("update T_USER set name=?,age=? where id=?", name, age, id);
        } else {
            userMapper.update(id, name, age);
        }
    }

    @Override
    public void delete(Long id) {
        if (jdbcMode) {
            jdbcTemplate.update("delete from T_USER where id = ?", id);
        } else {
            userMapper.delete(id);
        }
    }

    @Override
    public void deleteAll() {
        if (jdbcMode) {
            jdbcTemplate.update("delete from T_USER");
        } else {
            userMapper.deleteAll();
        }
    }
}