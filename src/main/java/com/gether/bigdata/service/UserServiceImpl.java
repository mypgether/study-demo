package com.gether.bigdata.service;

import com.gether.bigdata.dao.mapper.UserMapper;
import com.gether.bigdata.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * @author: myp
 * @date: 16/10/22
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements  UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMapper userMapper;

    private static final boolean jdbcMode = false;

    public static int i = 0;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void add(Long id, String name, Integer age) {
        if (jdbcMode) {
            jdbcTemplate.update("insert into T_USER(id,NAME, AGE) values(?, ?, ?)", id, name, age);
        } else {
            userMapper.insert(id, name, age);
            i = i + 1;
            //if (i == 3) {
            //    throw new RuntimeException("呀呀出错了");
            //}
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User getById(Long id) {
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void update(Long id, String name, Integer age) {
        if (jdbcMode) {
            jdbcTemplate.update("update T_USER set name=?,age=? where id=?", name, age, id);
        } else {
            userMapper.update(id, name, age);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void delete(Long id) {
        if (jdbcMode) {
            jdbcTemplate.update("delete from T_USER where id = ?", id);
        } else {
            userMapper.delete(id);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void deleteAll() {
        if (jdbcMode) {
            jdbcTemplate.update("delete from T_USER");
        } else {
            userMapper.deleteAll();
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void insertFlow() throws ParseException {
        if (jdbcMode) {
        } else {
        }
    }
}