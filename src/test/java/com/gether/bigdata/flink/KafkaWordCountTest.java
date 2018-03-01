package com.gether.bigdata.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class KafkaWordCountTest {

    public static final String TOPIC_INPUT = "stringtest";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "test");
        //FlinkKafkaConsumer010<String> kafkaConsumer = new FlinkKafkaConsumer010<String>(TOPIC_INPUT, new SimpleStringSchema(), properties);
        //Map<KafkaTopicPartition, Long> offsets = Maps.newHashMap();
        //offsets.put(new KafkaTopicPartition(TOPIC_INPUT, 0), 1L);
        //offsets.put(new KafkaTopicPartition(TOPIC_INPUT, 1), 1L);
        //offsets.put(new KafkaTopicPartition(TOPIC_INPUT, 2), 1L);
        //kafkaConsumer.setStartFromSpecificOffsets(offsets);
        //kafkaConsumer.setStartFromEarliest();
        //DataStream<String> stream = env.addSource(kafkaConsumer);
        DataStream<String> stream = env.addSource(new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                sourceContext.collectWithTimestamp("this is moyun peng hehe ", System.currentTimeMillis());
                sourceContext.collectWithTimestamp("1", System.currentTimeMillis());
                sourceContext.collectWithTimestamp("x2", System.currentTimeMillis());
                sourceContext.collectWithTimestamp("3", System.currentTimeMillis());
                sourceContext.collectWithTimestamp("3", System.currentTimeMillis());
                sourceContext.collectWithTimestamp("3", System.currentTimeMillis());
                Thread.sleep(10000);
            }

            @Override
            public void cancel() {

            }
        });
        DataStream<Tuple2<String, Integer>> windowCounts =
                stream
                        .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                            @Override
                            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                                String[] split = s.split("\\W+");
                                for (String temp : split) {
                                    if (temp.length() > 0) {
                                        collector.collect(new Tuple2<String, Integer>(temp, 1));
                                    }
                                }
                            }
                        })
                        .keyBy(0)
                        .timeWindow(Time.seconds(5))
                        .sum(1);
        windowCounts.print().setParallelism(1);
        env.execute("Kafka Flink Test");
    }
}