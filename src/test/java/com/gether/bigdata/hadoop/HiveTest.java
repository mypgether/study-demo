package com.gether.bigdata.hadoop;

import org.junit.Test;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author: myp
 * @date: 16/11/8
 */
public class HiveTest {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    @Test
    public void test()
            throws SQLException, URISyntaxException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Connection con = DriverManager.getConnection(
                "jdbc:hive2://10.211.55.6:10000/default", "", "");
        Statement stmt = con.createStatement();
        // show tables
        String sql = "show tables";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }

        // describe table
        //sql = "describe " + tableName;
        //System.out.println("Running: " + sql);
        //res = stmt.executeQuery(sql);
        //while (res.next()) {
        //    System.out.println(res.getString(1) + "\t" + res.getString(2));
        //}
        //
        //
        //sql = "select * from " + tableName;
        //res = stmt.executeQuery(sql);
        //while (res.next()) {
        //    System.out.println(String.valueOf(res.getString(1)) + "\t"
        //            + res.getString(2));
        //}
        ////sql = "insert into inner_table  (key) values('谁说不能插入的')";
        ////res = stmt.executeQuery(sql);
        ////System.err.println(JsonUtils.toJsonStrWithNull(res));
        //
        ////String filePath = HiveConnection.class.getClass().getResource("/hdfs/a.log").toURI().toString();
        //String filePath = "/Users/myp/Git/github/bigdata/src/main/resources/hdfs/a.log";
        //System.out.println(filePath);
        //// hdfs 里面的path
        ////sql = String.format("load data inpath '%s' into table inner_table", filePath);
        //
        //// 本地 里面的path(但是需要在hadoop机器上)  http://stackoverflow.com/questions/20986175/hive-no-files-matching-path-file-and-file-exists
        //tableName = "inner_table";
        ////sql = "LOAD DATA LOCAL INPATH '" + filePath + "' into table " + tableName;
        //sql = "LOAD DATA LOCAL INPATH '/usr/local/a.log' overwrite into table inner_table";
        //boolean exeTrue = stmt.execute(sql);
        //System.err.println(exeTrue);


        //String sql = "CREATE TABLE IF NOT EXISTS " + "flow_statistic_11" + " (appNO int,serverNo int,userNo string,deviceNo string,terminalNo string,terminalType int,dataSize int,liveTime double,flowType int,netType int,ip string,areaNo bigint,carrier string,version string,deviceMsg string, time bigint,remarks string) ROW FORMAT DELIMITED  FIELDS TERMINATED BY '\t'  LINES TERMINATED BY '\n' STORED AS TEXTFILE";
        //stmt.execute(sql);

        //String tableName = "inner_table";
        ////sql = "LOAD DATA LOCAL INPATH '" + filePath + "' into table " + tableName;
        //String sql = "LOAD DATA LOCAL INPATH '/usr/local/flowHive.log' overwrite into table flow_statistic_11";
        //boolean exeTrue = stmt.execute(sql);
        //System.err.println(exeTrue);
    }
}