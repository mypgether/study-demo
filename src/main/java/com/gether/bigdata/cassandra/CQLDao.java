package com.gether.bigdata.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.QueryLogger;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ThreadLocalMonotonicTimestampGenerator;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author: myp
 * @date: 16/10/28
 */
public class CQLDao implements InitializingBean, DisposableBean {

  private static final byte[] sync = new byte[0];

  private Cluster cluster = null;
  private Session session;

  private String addresses = "127.0.0.1";
  private int port = 9042;
  private String username;
  private String password;
  private String clusterName = "myCluster";

  private String keyspace = "dbtest";
  private String TABLENAME_EXAMPLE = "example";


  @Override
  public void afterPropertiesSet() {
    init();
  }

  private void init() {
    try {
      Cluster.Builder clusterBuilder = Cluster.builder();
      String[] adr = addresses.split(",");
      clusterBuilder.addContactPoints(adr);
      clusterBuilder.withPort(port);
      clusterBuilder.withClusterName(clusterName);
      PoolingOptions options = new PoolingOptions();
      options.setPoolTimeoutMillis(1000);
      //clusterBuilder.withPoolingOptions(options);
      // 自定义类型转换器
      //clusterBuilder.withCodecRegistry(null);
      clusterBuilder.withLoadBalancingPolicy(new RoundRobinPolicy());
      clusterBuilder.withMaxSchemaAgreementWaitSeconds(6000);
      clusterBuilder.withQueryOptions(new QueryOptions().setFetchSize(1));
      clusterBuilder.withTimestampGenerator(new ThreadLocalMonotonicTimestampGenerator());
      if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(username)) {
        clusterBuilder.withAuthProvider(new PlainTextAuthProvider(username, password));
      }

      cluster = clusterBuilder.build();
      QueryLogger queryLogger = QueryLogger.builder()
          .withConstantThreshold(500L)
          .withMaxQueryStringLength(100)
          .build();
      cluster.register(queryLogger);
      session = cluster.connect(keyspace);
    } catch (Throwable e) {
      e.printStackTrace();
      throw e;
    }
  }

  public Session getSession() {
    if (session == null) {
      synchronized (sync) {
        if (session == null) {
          init();
        }
      }
    }
    return session;
  }

  @Override
  public void destroy() throws Exception {
    if (cluster != null) {
      cluster.close();
    }
  }
}