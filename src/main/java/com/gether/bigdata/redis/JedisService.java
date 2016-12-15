package com.gether.bigdata.redis;

/**
 * Created by myp on 2016/12/5.
 */
public interface JedisService {

    public void init();

    public void destroy();

    public boolean delete(String key);

    public Long hset(String key, String filed, String value, Long expires);

    public String hget(String key, String filed);

    public String get(String key);

    public Object getObject(String key);

    public boolean set(String key, String value, int cacheSeconds);

    public boolean setObject(String key, Object value, int cacheSeconds);

    public boolean setnx(String key, String value, int cacheSeconds);
}