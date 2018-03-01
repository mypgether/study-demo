package com.gether.bigdata.jvm.java8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by myp on 2017/7/20.
 */
public class TryWithResourceTest {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://www.baidu.com").openStream()))) {
            String line = reader.readLine();
            SimpleDateFormat format = new SimpleDateFormat("MM/DD/YY");
            Date date = format.parse(line);
            System.out.println(date);
        } catch (ParseException | IOException exception) {
            exception.printStackTrace();
        }
    }
}