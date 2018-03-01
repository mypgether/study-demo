package com.gether.bigdata.okhttp;

/**
 * Created by myp on 2017/8/10.
 */
public class HttpRequestException extends RuntimeException {

    public HttpRequestException(String httpReslut) {
        super(httpReslut);
    }
}