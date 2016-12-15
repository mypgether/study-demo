package com.gether.bigdata.cache;

import com.gether.bigdata.redis.JedisClusterUtils;
import com.gether.bigdata.redis.JedisUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: myp
 * @date: 16/10/22
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class JedisTest {

    @Test
    public void test() throws Exception {
        for (int i = 0; i <= 10; i++) {
            System.out.println(JedisUtils.setnx("a", "avalue", -100));
        }
    }

    private JedisCluster jedisCluster = null;
    //private ShardedJedis shardedJedis;// 切片额客户端连接
    //private ShardedJedisPool shardedJedisPool;// 切片连接池
    private JedisPool jedisPool = null;

    private String clusters = "localhost:6379";
    private int maxIdle = 5;
    private int maxTotal = 50;
    private int minIdle = 5;
    private int maxWaitMillis = 10000;
    private boolean isBorrow = true;
    private boolean isCluster = false;
    private String password;

    private String keyPrefix = "esdserver";

    @Before
    public void before() {
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
                if (StringUtils.isNotBlank(this.password)) {
                    jedisPool = new JedisPool(config, host, port, 2000, password);
                } else {
                    jedisPool = new JedisPool(config, host, port);
                }
                JedisUtils.setJedisPool(jedisPool);
                //shardedJedisPool = new ShardedJedisPool(config, shards);
            } catch (Exception e) {
            }
        }
    }
}