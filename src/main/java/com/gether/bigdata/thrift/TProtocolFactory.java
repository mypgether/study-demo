package com.gether.bigdata.thrift;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class TProtocolFactory
    extends BasePooledObjectFactory<TProtocol> {

  private String host;
  private int port;
  private boolean keepAlive = true;
  public int TIMEOUT = 10000;

  public TProtocolFactory(String host, int port, boolean keepAlive, int timeout) {
    this.host = host;
    this.port = port;
    this.keepAlive = keepAlive;
    this.TIMEOUT = timeout;
  }

  @Override
  public TProtocol create() throws TTransportException {
    TTransport tTransport = new TSocket(host, port, TIMEOUT);
    //TTransport tTransport = new TFramedTransport(tSocket);
    TProtocol protocol = new TBinaryProtocol(tTransport);
    tTransport.open();

    //TTransport transport = new TFramedTransport(new TSocket(host, port, TIMEOUT));
    //// 协议要和服务端一致
    //TProtocol protocol = new TBinaryProtocol(transport);
    //transport.open();
    return protocol;
  }


  @Override
  public PooledObject<TProtocol> wrap(TProtocol protocol) {
    return new DefaultPooledObject<>(protocol);
  }


  /**
   * 对象激活(borrowObject时触发）
   */
  @Override
  public void activateObject(PooledObject<TProtocol> pooledObject) throws TTransportException {
    if (!pooledObject.getObject().getTransport().isOpen()) {
      pooledObject.getObject().getTransport().open();
    }
  }


  /**
   * 校验不通过重新连接或则回收对象
   */
  @Override
  public boolean validateObject(PooledObject<TProtocol> p) {
    if (p.getObject() != null) {
      if (p.getObject().getTransport().isOpen()) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  /**
   * 对象钝化(即：从激活状态转入非激活状态，returnObject时触发）
   */
  @Override
  public void passivateObject(PooledObject<TProtocol> pooledObject) throws TTransportException {
    if (!keepAlive) {
      pooledObject.getObject().getTransport().close();
    }
  }

  /**
   * 对象销毁(clear时会触发）
   */
  @Override
  public void destroyObject(PooledObject<TProtocol> pooledObject) throws TTransportException {
    if (pooledObject.getObject().getTransport().isOpen()) {
      pooledObject.getObject().getTransport().close();
    }
  }
}