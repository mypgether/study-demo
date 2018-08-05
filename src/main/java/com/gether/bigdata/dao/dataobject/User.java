package com.gether.bigdata.dao.dataobject;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author: myp
 * @date: 16/10/22
 */
public class User implements Serializable {

  private Long id;
  private String name;
  private Integer age;

  // 省略setter和getter

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this).toString();
  }
}