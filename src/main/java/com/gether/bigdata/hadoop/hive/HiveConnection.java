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

    private String url = "jdbc:hive2://10.211.55.6:10000/default";
    private String username = "";
    private String password = "";

    private Statement statement = null;
    private Connection connection = null;

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
        log.info("hive init start...");
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(
                    this.url, this.username, this.password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            log.error(driverName + " not found!", e);
            //System.exit(1);
        } catch (SQLException e) {
            log.error("Connection error!", e);
            //System.exit(1);
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