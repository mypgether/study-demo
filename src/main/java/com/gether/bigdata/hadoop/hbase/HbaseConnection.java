package com.gether.bigdata.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author: myp
 * @date: 16/10/26
 */
public class HbaseConnection {

  private static final Logger log = LoggerFactory.getLogger(HbaseConnection.class);

  private Connection connection = null;
  // zk服务地址
  private String zkQuorum;
  // hbase连接地址
  private String master;
  // session超时时间
  private String sessionTimeout;
  private boolean init;

  public void init() {
    if (init) {
      Configuration conf = HBaseConfiguration.create();
      String[] zkHostPort = this.zkQuorum.split(":");
      conf.set("hbase.master", this.master);
      conf.set("hbase.zookeeper.quorum", zkHostPort[0]);
      conf.set("hbase.zookeeper.property.clientPort", zkHostPort[1]);
      conf.set("zookeeper.session.timeout", this.sessionTimeout);
      try {
        connection = ConnectionFactory.createConnection(conf);
      } catch (IOException e) {
        log.error("init hbase error", e);
      }
      log.info("hbase连接成功！");
    }
  }

  public void destroy() {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (IOException e) {
    }
  }

  public void setInit(boolean init) {
    this.init = init;
  }

  /**
   * @param zkQuorum
   */
  public void setZkQuorum(String zkQuorum) {
    this.zkQuorum = zkQuorum;
  }

  /**
   * @param master
   */
  public void setMaster(String master) {
    this.master = master;
  }

  public void setSessionTimeout(String sessionTimeout) {
    this.sessionTimeout = sessionTimeout;
  }

  /**
   * @throws IOException
   */
  public void createTable(String tableName, String... familys) throws IOException {

    if (connection.getAdmin().tableExists(TableName.valueOf(tableName))) {
      log.info("{} 表已存在!", tableName);
    } else {
      log.info("start to create table, {}", tableName);
      HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
      for (String family : familys) {
        hTableDescriptor.addFamily(new HColumnDescriptor(family));
      }
      connection.getAdmin().createTable(hTableDescriptor);
      log.info("{} 表创建成功！", tableName);
    }
  }

  /**
   * 查询所有记录
   */
  public void getAllData(String tableName) {
    try {
      Table table = connection.getTable(TableName.valueOf(tableName));
      ResultScanner scanner = table.getScanner(new Scan());
      for (Result result : scanner) {
        if (result.raw().length > 0) {
          for (KeyValue kv : result.raw()) {
            System.out.println(new String(kv.getRow()) + "\t"
                + new String(kv.getValue()));
          }
        }
      }

      scanner = table.getScanner(new Scan());
      for (Result result : scanner) {
        /**
         * 这里用的都是keyValue里面旧的方法来获取行键，列族，列和值
         * 新的方法后面都有个Array，但是显示出来中间总有乱码，
         * 我猜测是时间戳在中间，但不知道怎么解析。
         * 以后再来解决
         */
        for (KeyValue keyValue : result.raw()) {
          System.err.println("row:" + new String(keyValue.getRow()) +
              "\tcolumnfamily:" + new String(keyValue.getFamily()) +
              "\tcolumn:" + new String(keyValue.getQualifier()) +
              "\tvalue:" + new String(keyValue.getValue()));
        }
      }

      scanner = table.getScanner(new Scan());
      for (Result r : scanner) {
        for (Cell cell : r.rawCells()) {
          //if (Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()).equals("cf") && Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()).equals("value")) {
          String value = Bytes
              .toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
          System.out.println(new String(r.getRow()) + "==>" + value);
          //}
        }
      }
    } catch (IOException e) {
      log.error("getAllData error tableName:{}", tableName, e);
    }
  }

  /**
   * 往表中添加一条数据
   *
   * @param family 列族
   * @param qualifier 列
   * @param value 值
   */
  public void addOneData(String tableName, String rowkey, String family,
      String qualifier, String value) throws IOException {
    Table table = connection.getTable(TableName.valueOf(tableName));
    Put put = new Put(rowkey.getBytes());
    put.addColumn(family.getBytes(), qualifier.getBytes(), value.getBytes());
    try {
      table.put(put);
    } catch (IOException e) {
      log.error("记录 {} 添加失败！", rowkey, e);
    }
  }

  /**
   * 获取所有的列表
   */
  private void getAllTable() throws IOException {
    if (connection.getAdmin() != null) {
      try {
        HTableDescriptor[] listTables = connection.getAdmin().listTables();
        if (listTables.length > 0) {
          for (HTableDescriptor hTableDescriptor : listTables) {
            System.out.println(hTableDescriptor.getNameAsString());
          }
        }
      } catch (IOException e) {
        log.error("getAll table error", e);
      }
    }
  }

  public void getOneRcord(String tableName, String rowkey) throws IOException {
    Table table = connection.getTable(TableName.valueOf(tableName));
    Get get = new Get(rowkey.getBytes());

    try {
      Result result = table.get(get);
      if (result.raw().length > 0) {
        for (KeyValue kv : result.raw()) {
          System.err.println(new String(kv.getRow()) + "\t" + new String(kv.getValue()));
        }
        System.out.println("\n");
        for (KeyValue kv : result.raw()) {
          System.out.println(new String(kv.getRowArray()) + "\t" + new String(kv.getValueArray()));
        }
        System.out.println("\n");
        for (KeyValue kv : result.raw()) {
          System.err.println(new String(kv.getKey()) + "\t" + new String(kv.getValue()));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 删除一条记录
   */
  public void deleteAllOnedata(String tableName, String rowkey) throws IOException {
    Table table = connection.getTable(TableName.valueOf(tableName));
    Delete delete = new Delete(rowkey.getBytes());
    try {
      table.delete(delete);
      log.info(rowkey + " 记录删除成功！");
    } catch (IOException e) {
      log.error(rowkey + " 记录删除失败！", e);
    }
  }

  /**
   * 删除一条记录的一个值
   */
  public void deleteOneRcord(String tableName, String rowkey, String family,
      String qualifier) throws IOException {
    Table table = connection.getTable(TableName.valueOf(tableName));
    Delete delete = new Delete(rowkey.getBytes());
    delete.deleteColumn(family.getBytes(), qualifier.getBytes());
    try {
      table.delete(delete);
      log.info(tableName + " " + rowkey + "," + family + ":" + qualifier + "值删除成功！");
    } catch (IOException e) {
      log.error(tableName + " " + rowkey + "," + family + ":" + qualifier + "值删除失败s！", e);
    }

  }

  /**
   * 删除一张表
   */
  public void deleteTable(String tableName) throws IOException {
    if (connection.getAdmin() != null) {
      try {
        connection.getAdmin().disableTable(TableName.valueOf(tableName));
        connection.getAdmin().deleteTable(TableName.valueOf(tableName));
        log.info("{} 表删除成功！", tableName);
      } catch (IOException e) {
        log.error("{} 表删除失败！", tableName, e);
      }
    }
  }
}