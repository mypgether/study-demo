package com.gether.bigdata;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author: myp
 * @date: 16/10/22
 */
public class FlowStatisticTest {

    @Test
    public void huidang输出() throws Exception {
        File f = new File("/Users/myp/Desktop/a.txt");
        File out = new File("/Users/myp/Desktop/aout.txt");
        List<String> lines = FileUtils.readLines(f);
        int result = 0;
        for (int i = 0; i < lines.size(); i++) {
            String nowLine = lines.get(i);
            String nextLine = null;
            if (i + 1 != lines.size()) {
                nextLine = lines.get(i + 1);
            }
            if (StringUtils.isBlank(nowLine)) continue;
            String[] splits = nowLine.split("\t");
            boolean needWrite = false;
            if (i + 1 != lines.size()) {
                // 不是最后一行
                String[] nextSplits = nextLine.split("\t");
                if (nextSplits.length != splits.length) {
                    needWrite = true;
                } else {
                    if (StringUtils.equals(splits[splits.length - 2].trim(), nextSplits[nextSplits.length - 2].trim())) {
                        needWrite = false;
                    } else {
                        needWrite = true;
                    }
                }
                System.err.println(splits[splits.length - 2].trim() + "-->" + splits[splits.length - 1].trim());
            } else {
                needWrite = true;
            }
            if (needWrite) {
                if (StringUtils.equals(splits[splits.length - 2].trim(), splits[splits.length - 1].trim())) {
                    result += Integer.parseInt(splits[splits.length - 2].trim());
                    FileUtils.write(out, splits[splits.length - 2].trim() + "\n", true);
                } else {
                    result += Integer.parseInt(splits[splits.length - 1].trim());
                    FileUtils.write(out, splits[splits.length - 1].trim() + "\n", true);
                }

            }
        }
        System.out.println(result);
    }

    @Test
    public void 统计播放器的回放流量总量() throws IOException {
        File out = new File("/Users/myp/Desktop/aout.txt");
        List<String> lines = FileUtils.readLines(out);
        int result = 0;
        for (int i = 0; i < lines.size(); i++) {
            String nowLine = lines.get(i);
            if (StringUtils.isNotBlank(nowLine)) {
                result += Integer.parseInt(nowLine.trim());
            }
        }
        System.out.println(result);
    }

    @Test
    public void 输出app打印的直播日志() throws IOException {
        File in = new File("/Users/myp/Desktop/zhibo.txt");
        File out = new File("/Users/myp/Desktop/zhiboout.txt");
        List<String> lines = FileUtils.readLines(in);
        int result = 0;
        for (int i = 0; i < lines.size(); i++) {
            String nowLine = lines.get(i);
            boolean needWrite = false;
            if (i + 1 != lines.size()) {
                String nextLine = lines.get(i + 1);
                String[] splits = nowLine.split(" ");
                String[] nextSplits = nextLine.split(" ");
                double nowInt = Double.parseDouble(splits[splits.length - 1].trim());
                Double nextInt = Double.parseDouble(nextSplits[nextSplits.length - 1].trim());
                if (nextInt >= nowInt) {
                    needWrite = false;
                } else {
                    needWrite = true;
                }
            } else {
                needWrite = true;
            }
            if (needWrite) {
                System.out.println(nowLine);
                FileUtils.write(out, nowLine.trim() + "\n", true);
                result += Double.parseDouble(nowLine.trim());
            }
        }
        System.out.println(result);
    }

    @Test
    public void 比较平台回放和播放器回放日志信息() throws IOException {
        // 播放器日志
        File bofangqi = new File("/Users/myp/Desktop/aout.txt");
        File pingtai = new File("/Users/myp/Desktop/pingtai.txt");
        List<String> bofangqiLines = FileUtils.readLines(bofangqi);
        List<String> pingtaiLines = FileUtils.readLines(pingtai);
        //统计平台有但是播放器没有的
        List<String> outString = Lists.newArrayList();
        for (String pt : pingtaiLines) {
            if (!bofangqiLines.contains(pt.trim())) {
                if (!outString.contains(pt)) {
                    outString.add(pt);
                    System.out.println(pt);
                }
            }
        }
        System.out.println("\n");

        //统计播放器有但是平台没有的
        List<String> outString2 = Lists.newArrayList();
        for (String bfq : bofangqiLines) {
            if (!pingtaiLines.contains(bfq.trim())) {
                if (!outString2.contains(bfq)) {
                    outString2.add(bfq);
                    System.out.println(bfq);
                }
            }
        }
    }

    @Test
    public void flowStatistic() throws ParseException {
        String country;
        String city;
        String flowtype;
        String nettype;
        Long size;
        Date time;
        String[] citys = new String[]{"杭州", "北京", "上海", "广州", "江苏", "宁波"};
        String[] nettypes = new String[]{"WIFI", "2G", "3G", "4G"};
        String[] flowtypes = new String[]{"直播", "回放", "下载", "图片"};
        String[] dates = new String[]{"2016-10-10", "2016-10-11", "2016-10-12",
                "2016-10-13", "2016-10-14", "2016-10-15", "2016-10-16",
                "2016-10-17", "2016-10-18", "2016-10-19", "2016-10-20", "2016-10-21", "2016-10-22",
                "2016-10-23", "2016-10-24", "2016-10-25", "2016-10-26", "2016-10-27", "2016-10-28"};
        Random sizeRandom = new Random();
        for (String date : dates) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            time = sdf.parse(date);
            Random timesRandom = new Random();
            Random cityRandom = new Random();
            Random nettypeRandom = new Random();
            Random flowtypeRandom = new Random();
            int times = 40 + timesRandom.nextInt(30);
            for (int i = 0; i < times; i++) {
                city = citys[cityRandom.nextInt(6)];
                nettype = nettypes[nettypeRandom.nextInt(4)];
                flowtype = flowtypes[flowtypeRandom.nextInt(4)];
                System.out.println(String.format("INSERT INTO flow_statistic(country,city,flowtype,nettype,size,time,createtime) VALUES('%s','%s','%s','%s',%s,'%s',now());", "中国", city, flowtype, nettype, Long.parseLong(20 + sizeRandom.nextInt(80) + ""), date));
            }
        }
    }

    @Test
    public void outzhongwen(){
        System.out.println("呵呵你好");
    }
}