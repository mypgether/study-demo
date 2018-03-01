package com.gether.bigdata.jvm.clazz;

/**
 * Created by myp on 2017/3/4.
 */
public class NoStack2Exception extends RuntimeException {
    public NoStack2Exception() {
    }

    public NoStack2Exception(String message) {
        super(message);
    }

    public NoStack2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public NoStack2Exception(Throwable cause) {
        super(cause);
    }

    public NoStack2Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}