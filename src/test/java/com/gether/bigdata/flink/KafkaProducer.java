package com.gether.bigdata.flink;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by myp on 2017/11/30.
 */
public class KafkaProducer {

    @Test
    public void produceAll() throws IOException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);
        List<String> items = readItem();
        items.forEach((String item) -> producer.send(new ProducerRecord<String, String>(KafkaPurchaseAnalysisTest.TOPIC_INPUT_ITEMS, String.valueOf(item.hashCode()), item)));

        items = readOrders();
        items.forEach((String item) -> producer.send(new ProducerRecord<String, String>(KafkaPurchaseAnalysisTest.TOPIC_INPUT_ORDERS, String.valueOf(item.hashCode()), item)));

        items = readUser();
        items.forEach((String item) -> producer.send(new ProducerRecord<String, String>(KafkaPurchaseAnalysisTest.TOPIC_INPUT_USERS, String.valueOf(item.hashCode()), item)));

        producer.close();
    }

    private List<String> readItem() throws IOException {
        List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/bigdata/src/test/java/com/gether/bigdata/flink/file/items.csv"));
        return lines.stream()
                .filter(StringUtils::isNotBlank)
                .filter((line) -> line.split("\\s*,\\s*").length == 4)
                .collect(Collectors.toList());
    }

    private List<String> readOrders() throws IOException {
        List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/bigdata/src/test/java/com/gether/bigdata/flink/file/orders.csv"));
        return lines.stream()
                .filter(StringUtils::isNotBlank)
                .filter((line) -> line.split("\\s*,\\s*").length == 4)
                .collect(Collectors.toList());
    }

    private List<String> readUser() throws IOException {
        List<String> lines = FileUtils.readLines(new File("/Users/myp/Git/github/bigdata/src/test/java/com/gether/bigdata/flink/file/users.csv"));
        return lines.stream()
                .filter(StringUtils::isNotBlank)
                .filter((line) -> line.split("\\s*,\\s*").length == 4)
                .collect(Collectors.toList());
    }
}