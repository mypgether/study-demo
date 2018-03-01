package com.gether.bigdata.guava;

import com.gether.bigdata.dao.dataobject.User;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by myp on 2016/12/28.
 */
public class GuavaCacheTest {

    private static Cache<String, User> poiCache = CacheBuilder.newBuilder().maximumSize(100000).expireAfterAccess(6, TimeUnit.HOURS).build();

    @Test
    public void testNull(){
        User model = null;
        System.out.println(Optional.fromNullable(model).or(new User()));
        System.out.println(new User().toString());
    }

    @Test
    public void getCache() throws ExecutionException {
        final String id = "1";
        User model = poiCache.get(id, new Callable<User>() {
            @Override
            public User call() {
                User u = new User();
                u.setId(Long.parseLong(id));
                u.setName("gether");
                System.out.println("in call");
                return u;
            }
        });
        System.out.println(model.getId());
    }
}
