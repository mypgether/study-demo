package com.gether.bigdata.mycat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by myp on 2017/4/26.
 */
public class MycatDbTest {

  public static void main(String[] args) throws SQLException {
    String driver = "com.mysql.jdbc.Driver";
    String dbName = "TESTDB";
    String passwrod = "123456";
    String userName = "root";
    String url = "jdbc:mysql://localhost:8066/" + dbName;
    Connection conn = null;
    try {
      Class.forName(driver);
      conn = DriverManager.getConnection(url, userName, passwrod);
      //conn.setAutoCommit(false);
      PreparedStatement ps = conn.prepareStatement("update device set name='哈哈' where id=1;");
      int row = ps.executeUpdate();
      System.out.println("update row " + row);

      ps = conn.prepareStatement("select * from device where id=1;");
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        System.out.println("name : " + rs.getString("name"));
      }

      ps = conn.prepareStatement("insert into device (device_id,name,uid) values('d1','name',2);");
      ps.execute();
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      //conn.rollback();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }
}