package com.gether.bigdata.redis;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by myp on 2016/12/5.
 */
public class JedisServiceImpl implements JedisService {

    private static Logger log = LoggerFactory.getLogger(JedisServiceImpl.class);

    private JedisCluster jedisCluster = null;
    //private ShardedJedis shardedJedis;// 切片额客户端连接
    //private ShardedJedisPool shardedJedisPool;// 切片连接池
    private JedisPool jedisPool = null;

    private String clusters;
    private int maxIdle = 5;
    private int maxTotal = 50;
    private int minIdle = 5;
    private int maxWaitMillis = 10000;
    private boolean isBorrow = true;
    private boolean isCluster = false;
    private String password;

    private String keyPrefix = "esdserver";


    @Override
    public void init() {
        if (this.isCluster) {
            try {
                Set<HostAndPort> jedisClusterNodes = new HashSet<>();
                String host = "";
                int port = 6379;
                String[] ipArr = clusters.split(",");
                for (String ipPort : ipArr) {
                    String[] hostPort = ipPort.split(":");
                    host = hostPort[0];
                    port = Integer.parseInt(hostPort[1]);
                    jedisClusterNodes.add(new HostAndPort(host, port));
                }
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxIdle(this.maxIdle);
                config.setMaxTotal(this.maxTotal);
                config.setMinIdle(this.minIdle);
                config.setMaxWaitMillis(this.maxWaitMillis);
                config.setTestOnBorrow(this.isBorrow);
                config.setTestOnReturn(true);
                jedisCluster = new JedisCluster(jedisClusterNodes, config);
                JedisClusterUtils.setJedisCluster(jedisCluster);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                String host = "";
                int port = 6379;
                String[] ipArr = clusters.split(",");
                // slave链接
                //List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
                for (String ipPort : ipArr) {
                    String[] hostPort = ipPort.split(":");
                    host = hostPort[0];
                    port = Integer.parseInt(hostPort[1]);
                    //JedisShardInfo info = new JedisShardInfo(hostPort[0], Integer.parseInt(hostPort[1]), hostPort[0].replace(".", ""));
                    //info.setPassword(this.password);
                    //shards.add(info);
                }
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxIdle(this.maxIdle);
                config.setMaxTotal(this.maxTotal);
                config.setMinIdle(this.minIdle);
                config.setMaxWaitMillis(this.maxWaitMillis);
                config.setTestOnBorrow(this.isBorrow);
                config.setTestOnReturn(true);
                log.info("{},{},{}", host, port, password);
                if (StringUtils.isNotBlank(this.password)) {
                    jedisPool = new JedisPool(config, host, port, 2000, password);
                } else {
                    jedisPool = new JedisPool(config, host, port);
                }
                JedisUtils.setJedisPool(jedisPool);
                //shardedJedisPool = new ShardedJedisPool(config, shards);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        try {
            if (this.isCluster) {
                if (null != jedisCluster) {
                    jedisCluster.close();
                    jedisCluster = null;
                }
            } else {
                if (null != jedisPool) {
                    jedisPool.close();
                    jedisPool = null;
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public boolean delete(String key) {
        key = buildKey(key);
        if (this.isCluster) {
            return JedisClusterUtils.del(key);
        } else {
            long result = JedisUtils.del(key);
            if (result == 1) {
                return true;
            }
            return false;
        }
    }

    @Override
    public Long hset(String key, String filed, String value, Long expires) {
        key = buildKey(key);
        if (this.isCluster) {
            Long result = JedisClusterUtils.hSet(key, filed, value);
            return result;
        } else {
            Long result = JedisUtils.hset(key, filed, value);
            return result;
        }
    }

    @Override
    public String hget(String key, String filed) {
        key = buildKey(key);
        if (this.isCluster) {
            return JedisClusterUtils.hGet(key, filed);
        } else {
            return JedisUtils.hGet(key, filed);
        }
    }

    @Override
    public String get(String key) {
        key = buildKey(key);
        if (this.isCluster) {
            return JedisClusterUtils.get(key, null);
        } else {
            return JedisUtils.get(key);
        }
    }

    @Override
    public Object getObject(String key) {
        key = buildKey(key);
        if (this.isCluster) {
            return JedisClusterUtils.getObject(key);
        } else {
            return JedisUtils.getObject(key);
        }
    }

    @Override
    public boolean set(String key, String value, int cacheSeconds) {
        key = buildKey(key);
        if (this.isCluster) {
            if (cacheSeconds > 0) {
                return JedisClusterUtils.set(key, value, cacheSeconds);
            } else {
                return JedisClusterUtils.set(key, value);
            }
        } else {
            String result = JedisUtils.set(key, value, cacheSeconds);
            return true;
        }
    }

    @Override
    public boolean setObject(String key, Object value, int cacheSeconds) {
        key = buildKey(key);
        if (this.isCluster) {
            return JedisClusterUtils.setObject(key, value, cacheSeconds);
        } else {
            String result = JedisUtils.setObject(key, value, cacheSeconds);
            if (StringUtils.equalsIgnoreCase(result, "OK")) {
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean setnx(String key, String value, int cacheSeconds) {
        key = buildKey(key);
        if (this.isCluster) {
            return JedisClusterUtils.setnx(key, value, cacheSeconds);
        } else {
            Long result = JedisUtils.setnx(key, value, cacheSeconds);
            if (result == 1)
                return true;
        }
        return false;
    }

    private String buildKey(String key) {
        return keyPrefix + key;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public void setBorrow(boolean borrow) {
        isBorrow = borrow;
    }

    public void setCluster(boolean cluster) {
        isCluster = cluster;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}