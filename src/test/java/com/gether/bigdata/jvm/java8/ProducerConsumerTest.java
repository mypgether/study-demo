package com.gether.bigdata.jvm.java8;


/**
 * Created by myp on 2017/7/25.
 */
public class ProducerConsumerTest {

    //public static void main(String[] args) throws InterruptedException {
    //
    //    // Object on which producer and consumer thread will operate
    //    ProducerConsumerImpl sharedObject = new ProducerConsumerImpl();
    //
    //    // creating producer and consumer threads
    //    producer p = new producer(sharedObject);
    //    Consumer c = new Consumer(sharedObject);
    //
    //    // starting producer and consumer threads
    //    p.start();
    //    c.start();3
    //
    //    p.join();
    //    c.join();
    //}
    public static void main(String[] args) throws InterruptedException {

        // Object on which producer and consumer thread will operate
        com.gether.research.jvm.producer.ShareObjService sharedObject = new com.gether.research.jvm.producer.ShareObjService();

        // creating producer and consumer threads
        com.gether.bigdata.jvm.producer.Producer p = new com.gether.bigdata.jvm.producer.Producer(sharedObject);
        com.gether.bigdata.jvm.producer.Consumer c = new com.gether.bigdata.jvm.producer.Consumer(sharedObject);

        // starting producer and consumer threads
        p.start();
        c.start();

        p.join();
        c.join();
    }
}