package com.gether.bigdata.domain.user;

/**
 * @author: myp
 * @date: 16/10/22
 */
public class User {

    //@Value("${user.name}")
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
}

