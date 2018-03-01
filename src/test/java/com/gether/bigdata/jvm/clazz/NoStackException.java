package com.gether.bigdata.jvm.clazz;

/**
 * Created by myp on 2017/3/4.
 */
public class NoStackException extends RuntimeException {

    public NoStackException() {
        super();
        this.setStackTrace(new StackTraceElement[0]);
    }

    public NoStackException(String message) {
        super(message);
        this.setStackTrace(new StackTraceElement[0]);
    }

    public NoStackException(String message, Throwable cause) {
        super(message, cause);
        this.setStackTrace(new StackTraceElement[0]);
    }

    public NoStackException(Throwable cause) {
        super(cause);
        this.setStackTrace(new StackTraceElement[0]);
    }

    protected NoStackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.setStackTrace(new StackTraceElement[0]);
    }
}