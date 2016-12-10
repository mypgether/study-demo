package com.gether.bigdata.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: myp
 * @date: 16/11/3
 */
public class IKafkaConsumer {
    public static final boolean running = true;

    private KafkaConsumer<String, String> consumer;

    public void consume(String consumerId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "consumer-group");
        props.put("enable.auto.commit", "true");  //自动commit
        //props.put("auto.commit.interval.ms", "1000"); //定时commit的周期
        //props.put("fetch.min.bytes", "1"); //每次最小拉取的消息大小byte
        //props.put("max.partition.fetch.bytes", "1048576"); //每次从单个分区中拉取的消息最大尺寸（byte）
        //props.put("max.poll.records", "20"); //限制一次poll的条目数
        props.put("session.timeout.ms", "6000"); //consumer活性超时时间
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        consumer = new KafkaConsumer<>(props);


        //consumer.subscribe(Arrays.asList("hehe2", "hehe"));
        consumer.subscribe(Arrays.asList("hehe"));
        long start = System.currentTimeMillis();
        try {
            while (running) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(10000);
                    //Thread.sleep(90000);r
                    System.err.println("out-->" + records.count() + " psize:" + records.partitions().size());
                    for (TopicPartition partition : records.partitions()) {
                        System.err.print(partition.partition() + "==>");
                    }
                    if (records.count() == 0) {
                        System.out.println((System.currentTimeMillis() - start) / 1000);
                        System.exit(0);
                    }
                    //for (ConsumerRecord<String, String> record : records) {
                    //System.err.println("topicName:" + record.topic() + " partition:" + record.partition() + " offset:" + record.offset() + " record:" + record.value());
                    //System.err.println("consumerId:" + consumerId + " partition:" + record.partition() + " offset:" + record.offset() + " record:" + record.value());
                    //System.err.println("keySize:" + record.serializedKeySize() + " ,valueSize:" + record.serializedValueSize());
                    //}
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } finally {
            consumer.close();
        }
    }

    public void streamConsume() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "consumer-group2");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        StreamsConfig config = new StreamsConfig(props);

        KStreamBuilder builder = new KStreamBuilder();
        //builder.stream("hehe").mapValues(value -> value.length().toString()).to("my-output-topic");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
    }

    public void stopConsume() {
        if (null != consumer) {
            consumer.wakeup();
        }
    }
}