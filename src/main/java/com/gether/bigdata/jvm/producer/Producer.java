package com.gether.bigdata.jvm.producer;

/**
 * Created by myp on 2017/8/13.
 */
public class Producer extends Thread {

  com.gether.research.jvm.producer.ShareObjService shareObjService;

  public Producer(com.gether.research.jvm.producer.ShareObjService shareObjService) {
    this.shareObjService = shareObjService;
  }

  @Override
  public void run() {
    shareObjService.produce();
  }
}