package com.gether.bigdata.thrift;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class TTransportFactory
        extends BasePooledObjectFactory<TTransport> {
    private String host;
    private int port;
    private boolean keepAlive = true;
    public int TIMEOUT = 10000;

    public TTransportFactory(String host, int port, boolean keepAlive, int timeout) {
        this.host = host;
        this.port = port;
        this.keepAlive = keepAlive;
        this.TIMEOUT = timeout;
    }

    @Override
    public TTransport create() throws TTransportException {
        //TTransport tTransport = new TSocket(host, port, TIMEOUT);
        ////TTransport tTransport = new TFramedTransport(tSocket);
        //TProtocol protocol = new TBinaryProtocol(tTransport);
        //tTransport.open();

        TTransport transport = new TFramedTransport(new TSocket(host, port, TIMEOUT));
        transport.open();
        return transport;
    }


    @Override
    public PooledObject<TTransport> wrap(TTransport transport) {
        return new DefaultPooledObject<>(transport);
    }


    /**
     * 对象激活(borrowObject时触发）
     *
     * @param pooledObject
     * @throws TTransportException
     */
    @Override
    public void activateObject(PooledObject<TTransport> pooledObject) throws TTransportException {
        if (!pooledObject.getObject().isOpen()) {
            pooledObject.getObject().open();
        }
    }


    /**
     * 校验不通过重新连接或则回收对象
     */
    @Override
    public boolean validateObject(PooledObject<TTransport> p) {
        if (p.getObject() != null) {
            if (p.getObject().isOpen()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 对象钝化(即：从激活状态转入非激活状态，returnObject时触发）
     *
     * @param pooledObject
     * @throws TTransportException
     */
    @Override
    public void passivateObject(PooledObject<TTransport> pooledObject) throws TTransportException {
        if (!keepAlive) {
            pooledObject.getObject().close();
        }
    }

    /**
     * 对象销毁(clear时会触发）
     *
     * @param pooledObject
     * @throws TTransportException
     */
    @Override
    public void destroyObject(PooledObject<TTransport> pooledObject) throws TTransportException {
        if (pooledObject.getObject().isOpen()) {
            pooledObject.getObject().close();
        }
        pooledObject.markAbandoned();
    }
}