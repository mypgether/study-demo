package com.gether.bigdata.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;

/**
 * @author: myp
 * @date: 16/11/3
 */
public class IKafkaProducer {
    KafkaProducer<String, String> producer = null;

    public void produce() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "1"); //ack方式，all，会等所有的commit最慢的方式
        props.put("retries", 0); //失败是否重试，设置会有可能产生重复数据
        props.put("batch.size", 16384); //对于每个partition的batch buffer大小
        props.put("linger.ms", 1);  //等多久，如果buffer没满，比如设为1，即消息发送会多1ms的延迟，如果buffer没满
        props.put("buffer.memory", 33554432); //整个producer可以用于buffer的内存大小

        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        producer = new KafkaProducer<String, String>(props);
        String msg = new Date() + " - hello world : 测试 ";
        for (int i = 0; ; i++) {
            producer.send(new ProducerRecord<String, String>("hehe", Integer.toString(i), msg + i), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null)
                        e.printStackTrace();
                    //System.err.println("message send to hehe partition " + metadata.partition() + ", offset: " + metadata.offset());
                }
            });
            //producer.send(new ProducerRecord<String, String>("hehe2", Integer.toString(i), msg + i), new Callback() {
            //    public void onCompletion(RecordMetadata metadata, Exception e) {
            //        if (e != null)
            //            e.printStackTrace();
            //        //System.err.println("message send to hehe2 partition " + metadata.partition() + ", offset: " + metadata.offset());
            //    }
            //});
            //try {
            //    Thread.sleep(1000);
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
            //}
        }
    }

    public void destory() {
        if (null != producer) {
            producer.close();
        }
    }
}