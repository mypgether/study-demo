package com.gether.bigdata.jvm.java8;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by myp on 2017/7/20.
 */
public class DatetimeTest {

    @Test
    public void testDate() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Paris"));
        System.out.println(localDateTime.toString());
        localDateTime = localDateTime.withYear(2008);
        System.out.println(localDateTime.toString());

        localDateTime.plusWeeks(4);
        System.out.println(localDateTime.toString());
        localDateTime = localDateTime.plusWeeks(4);
        System.out.println(localDateTime.toString());
        System.out.println(Thread.currentThread().getId());
    }

    @Test
    public void testDateDiff() {
        DateTime a = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2017-09-11 14:00:00");
        DateTime b = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2017-09-12 14:00:00");

        System.out.println(Days.daysBetween(a, b).getDays());

        a = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2017-09-11 14:00:00").withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        b = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2017-09-12 13:00:00").withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        System.out.println(Days.daysBetween(a, b).getDays());
    }
}