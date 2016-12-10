package com.gether.bigdata.hadoop;

import com.gether.bigdata.kafka.IKafkaConsumer;
import com.gether.bigdata.kafka.IKafkaProducer;
import org.junit.Test;

/**
 * @author: myp
 * @date: 16/11/3
 */
public class KafkaTest {


    @Test
    public void comsume1() {
        IKafkaConsumer consumer = new IKafkaConsumer();
        consumer.consume("consumer1");
        consumer.stopConsume();
    }

    @Test
    public void comsume2() {
        IKafkaConsumer consumer = new IKafkaConsumer();
        consumer.consume("consumer2");
        consumer.stopConsume();
    }

    @Test
    public void comsume3() {
        IKafkaConsumer consumer = new IKafkaConsumer();
        consumer.consume("consumer3");
        consumer.stopConsume();
    }

    @Test
    public void produce() {
        IKafkaProducer producer = new IKafkaProducer();
        producer.produce();
    }
}