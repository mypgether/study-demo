package com.gether.bigdata.thrift;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

public class WaterMarkMain {

  public static final String SERVER_IP = "localhost";
  public static final int SERVER_PORT = 9098;
  public static final int TIMEOUT = 30000;
  public static final int HASH_LENGTH = 32;

  @Test
  public void testBIO() {
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxTotal(200);
    poolConfig.setMinIdle(5);//最小空闲数
    poolConfig.setMaxIdle(10);//最大空闲数
    poolConfig.setBlockWhenExhausted(true);
    poolConfig.setMaxWaitMillis(2000);//最长等待时间
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    ObjectPool<TProtocol> pool = new GenericObjectPool<>(
        new TProtocolFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
    GetUserName.Client client;
    TProtocol protocol = null;
    long startTime = System.currentTimeMillis();
    for (int j = 0; j <= 10000; j++) {
      try {
        Thread.sleep(1000);
        protocol = null;
        protocol = pool.borrowObject();
        System.out.println("active:" + pool.getNumActive());
        System.out.println(protocol.toString());
        long startTime2 = System.currentTimeMillis();
        client = new GetUserName.Client(protocol);
        System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

        List<String> resultList = Lists.newArrayList();
        long startTime1 = System.currentTimeMillis();
        int fontSize = 16;
        String result = client.pdProcess("", fontSize, 100);
        resultList.add(result);
        System.out.println("cost1:" + (System.currentTimeMillis() - startTime1));
        print(result, fontSize);
        //System.out.println(result.getBytes().length);
        System.out.println("Thrify client result =: " + result);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (null != protocol) {
            pool.returnObject(protocol);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("cost:" + (System.currentTimeMillis() - startTime));
  }

  @Test
  public void testNIO() {
    long startTime = System.currentTimeMillis();
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxTotal(200);
    poolConfig.setMinIdle(5);//最小空闲数
    poolConfig.setMaxIdle(10);//最大空闲数
    poolConfig.setBlockWhenExhausted(true);
    poolConfig.setMaxWaitMillis(2000);//最长等待时间
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    ObjectPool<TProtocol> pool = new GenericObjectPool<>(
        new TProtocolFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
    GetUserName.Client client;
    TProtocol protocol = null;
    for (int j = 0; j <= 10000; j++) {
      try {
        Thread.sleep(1000);
        long startTime2 = System.currentTimeMillis();
        protocol = null;
        protocol = pool.borrowObject();
        //System.out.println("active:" + pool.getNumActive());
        client = new GetUserName.Client(protocol);
        //System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

        List<String> resultList = Lists.newArrayList();
        long startTime1 = System.currentTimeMillis();
        int fontSize = 1032;
        String result = client.pdProcess("", fontSize, 100);
        resultList.add(result);
        System.out.println("cost1:" + (System.currentTimeMillis() - startTime1));
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (TException e) {
        if (protocol != null) {
          protocol.getTransport().close();
        }
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (null != protocol) {
            pool.returnObject(protocol);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("cost:" + (System.currentTimeMillis() - startTime));
  }

  @Test
  public void testNIOWithTrasnportPoll() {
    long startTime = System.currentTimeMillis();
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxTotal(200);
    poolConfig.setMinIdle(5);//最小空闲数
    poolConfig.setMaxIdle(10);//最大空闲数
    poolConfig.setBlockWhenExhausted(true);
    poolConfig.setMaxWaitMillis(2000);//最长等待时间
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    ObjectPool<TTransport> pool = new GenericObjectPool<>(
        new TTransportFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
    GetUserName.Client client;
    TTransport transport = null;
    for (int j = 0; j <= 1000; j++) {

      try {
        Thread.sleep(1000);
        long startTime2 = System.currentTimeMillis();
        transport = null;
        transport = pool.borrowObject();
        System.out.println("active:" + pool.getNumActive());
        System.out.println("cost3:" + (System.currentTimeMillis() - startTime2));
        TBinaryProtocol protocol = new TBinaryProtocol(transport);
        client = new GetUserName.Client(protocol);
        System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

        List<String> resultList = Lists.newArrayList();
        int fontSize = 32;
        String result = client.pdProcess(null, fontSize, 10);
        resultList.add(result);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (TException e) {
        if (transport != null) {
          transport.close();
        }
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (null != transport) {
            pool.returnObject(transport);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("cost:" + (System.currentTimeMillis() - startTime));
  }

  @Test
  public void testNIOWithClienPoll() {
    long startTime = System.currentTimeMillis();
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxTotal(200);
    poolConfig.setMinIdle(5);//最小空闲数
    poolConfig.setMaxIdle(10);//最大空闲数
    poolConfig.setBlockWhenExhausted(true);
    poolConfig.setMaxWaitMillis(2000);//最长等待时间
    poolConfig.setTestOnBorrow(true);
    poolConfig.setTestOnReturn(true);
    ObjectPool<GetUserName.Client> pool = new GenericObjectPool<>(
        new TGetUserNameClientFactory(SERVER_IP, SERVER_PORT, true, TIMEOUT), poolConfig);
    GetUserName.Client client = null;
    for (int j = 0; j <= 1000; j++) {

      try {
        Thread.sleep(1000);
        long startTime2 = System.currentTimeMillis();
        client = null;
        client = pool.borrowObject();
        System.out.println("active:" + pool.getNumActive());
        System.out.println("cost2:" + (System.currentTimeMillis() - startTime2));

        List<String> resultList = Lists.newArrayList();
        String result = client.pdProcess(null, 1, 100);
        resultList.add(result);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (TException e) {
        if (client != null) {
          TTransport t1 = client.getInputProtocol().getTransport();
          TTransport t2 = client.getOutputProtocol().getTransport();
          System.out.println(t1 == t2);
          client.getInputProtocol().getTransport().close();
          client.getOutputProtocol().getTransport().close();
        }
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        try {
          if (null != client) {
            pool.returnObject(client);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("cost:" + (System.currentTimeMillis() - startTime));
  }

  private static void print(String sb, int fontSize) {
    int count = 0;
    for (int j = 0; j < fontSize; j++) {
      for (int i = 0; i < fontSize; i++) {
        if (sb.charAt(count) == '0') {
          System.out.print("-");
        } else if (sb.charAt(count) == '1') {
          System.out.print("*");
        }
        count++;
      }
      System.out.println("");
    }
  }
}