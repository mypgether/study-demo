package com.gether.bigdata.dao.mapper;

import com.gether.bigdata.dao.dataobject.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from T_USER where id=#{id}")
    User getById(@Param("id") Long id);

    @Select("select id,name,age from T_USER")
    List<User> getAll();

    @Delete("delete from T_USER where id = #{id}")
    int delete(@Param("id") Long id);

    @Update("update T_USER set name=#{name},age=#{age} where id=#{id}")
    void update(@Param("id") Long id, @Param("name") String name, @Param("age") Integer age);

    @Delete("delete from T_USER")
    int deleteAll();

    @Insert("INSERT INTO T_USER(id,NAME, AGE) VALUES(#{id},#{name}, #{age})")
    int insert(@Param("id") Long id, @Param("name") String name, @Param("age") Integer age);


    @Insert("INSERT INTO flow_statistic(country,city,flowtype,nettype,size,time,createtime) VALUES(#{country},#{city},#{flowtype},#{nettype},#{size},#{time},now())")
    int insertFlow(@Param("country") String country, @Param("city") String city, @Param("flowtype") String flowtype, @Param("nettype") String nettype, @Param("size") Long size, @Param("time") Date time);
}