package com.gether.bigdata.util;

/**
 * Created by myp on 2017/3/4.
 */
public class StringBuilderHelper {

    private final StringBuilder sb = new StringBuilder();

    public StringBuilderHelper() {
    }

    //private static final ThreadLocal<StringBuilderHelper>
    //        threadLocalStringBuilderHelper = new ThreadLocal<StringBuilderHelper>() {
    //    @Override
    //    protected StringBuilderHelper initialValue() {
    //        return new StringBuilderHelper();
    //    }
    //};

    public StringBuilder getStringBuilder() {
        sb.setLength(0);
        return sb;
    }
}