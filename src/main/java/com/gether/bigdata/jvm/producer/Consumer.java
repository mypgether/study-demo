package com.gether.bigdata.jvm.producer;

/**
 * Created by myp on 2017/8/13.
 */
public class Consumer extends Thread {


    com.gether.research.jvm.producer.ShareObjService shareObjService;

    public Consumer(com.gether.research.jvm.producer.ShareObjService shareObjService) {
        this.shareObjService = shareObjService;
    }

    @Override
    public void run() {
        shareObjService.consumer();
    }
}