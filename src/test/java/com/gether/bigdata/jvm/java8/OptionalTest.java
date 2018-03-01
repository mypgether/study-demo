package com.gether.bigdata.jvm.java8;

import org.junit.Test;

import java.util.Optional;

/**
 * Created by myp on 2017/8/1.
 */
public class OptionalTest {

    @Test
    public void testNull() {
        Object o = "this is string";
        System.out.println(Optional.ofNullable(o).orElse(1));
        o = null;
        System.out.println(Optional.ofNullable(o).orElse(1));

        o = null;
        System.out.println(Optional.ofNullable(o).orElseThrow(NullPointerException::new));
    }

    @Test
    public void testOptinalMap() {
        User user = new User("gether", 28);
        System.out.println(Optional.of(user).map((u) -> u.getName()).map(String::toUpperCase).orElse("Empty Name"));

        user.setName(null);
        System.out.println(Optional.of(user).map((u) -> u.getName()).map(String::toUpperCase).orElse("Empty Name"));

        user.setAge(0);
        System.out.println(Optional.of(user).map((u) -> u.getAge()).orElse(-1));
    }

    public class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}