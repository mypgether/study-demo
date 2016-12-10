package com.gether.bigdata.redis;

/**
 * Created by myp on 2016/12/5.
 */
public interface JedisService {

    public void init();

    public void destroy();

    public Long hset(String key, String filed, String value, Long expires);

    public String hget(String key, String filed);

}