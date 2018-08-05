package com.gether.bigdata.thrift;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUserNameClient {

  private Logger log = LoggerFactory.getLogger(GetUserNameClient.class);

  private GenericObjectPoolConfig thriftPoolConfig;

  private ObjectPool<TProtocol> pool;

  private String hostport;
  private int timeout;

  public void setThriftPoolConfig(GenericObjectPoolConfig thriftPoolConfig) {
    this.thriftPoolConfig = thriftPoolConfig;
  }

  public void setHostport(String hostport) {
    this.hostport = hostport;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  @PostConstruct
  public void WaterMarkClientInit() {
//    String[] hostports = hostport.split(":");
//    pool = new GenericObjectPool<TProtocol>(
//        new TProtocolFactory(hostports[0], Integer.valueOf(hostports[1]), true, timeout),
//        thriftPoolConfig);
  }

  public byte[] getWaterMarkByte() throws Exception {
    TProtocol protocol = null;
    try {
      protocol = pool.borrowObject();
      List<String> resultList = Lists.newArrayList();
      String[] hostports = hostport.split(":");
      GetUserName.Client client = new GetUserName.Client(protocol);
      String result = client.pdProcess(null, 1, 0);
      return null;
    } finally {
      try {
        if (null != protocol) {
          pool.returnObject(protocol);
        }
      } catch (Exception e) {
        log.error("thrift pool return error", e);
      }
    }
  }
}