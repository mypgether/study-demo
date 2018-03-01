package com.gether.bigdata.hadoop;

import com.gether.bigdata.hadoop.hbase.HbaseConnection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by myp on 2016/11/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class HbaseTest {


    @Autowired
    private HbaseConnection hBase;

    @Test
    public void hbaseTest() throws Exception {
        hBase.createTable("a12", "appno",
                "serverno",
                "userno",
                "deviceno",
                "terminalno",
                "terminaltype",
                "datasize",
                "livetime",
                "flowtype",
                "nettype",
                "ip",
                "areano",
                "carrier",
                "version",
                "devicemsg",
                "time",
                "remarks");

        //创建表
        hBase.createTable("Tabletest_1", "info", "scores");
        hBase.createTable("Tabletest_2", "info", "scores", "gether");

        //插入一条记录
        //hBase.addOneData("Tabletest", "one", "info", "weight", "50kg");
        hBase.addOneData("Tabletest_1", "one", "info", "hight", "175cm");
        //获得所有表中记录
        hBase.getAllData("Tabletest_1");
        //获得表中一行记录
        //hBase.getOneRcord("Tabletest", "one");
        //
        ////删除表中一条记录
        //hBase.deleteOneRcord("Tabletest", "one", "info", "hight");
        //
        ////删除表中一行记录
        //hBase.deleteAllOnedata("Tabletest", "one");
        //
        ////删除一个表
        ////hBase.deleteTable("Tabletest");
        //
        //hBase.getAllTable();
    }
}