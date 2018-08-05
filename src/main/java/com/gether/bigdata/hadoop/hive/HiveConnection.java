package com.gether.bigdata.hadoop.hive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author: myp
 * @date: 16/11/8
 */
public class HiveConnection {

  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private static String driverName = "org.apache.hive.jdbc.HiveDriver";

  private String url = "jdbc:hive2://localhost:10000/default";
  private String username = "";
  private String password = "";
  private boolean init;

  private Statement statement = null;
  private Connection connection = null;

  public void setInit(boolean init) {
    this.init = init;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void init() throws SQLException, ClassNotFoundException {
    if (init) {
      log.info("hive init start...");
      try {
        Class.forName(driverName);
        connection = DriverManager.getConnection(
            this.url, this.username, this.password);
        statement = connection.createStatement();
      } catch (ClassNotFoundException e) {
        log.error(driverName + " not found!", e);
      } catch (SQLException e) {
        log.error("Connection error!", e);
      }
    }
  }

  public void destory() {
    try {
      if (statement != null) {
        statement.close();
        statement = null;
      }
      if (connection != null) {
        connection.close();
        connection = null;
      }
    } catch (SQLException e) {
      log.error("hivedestory error", e);
    }
  }
}