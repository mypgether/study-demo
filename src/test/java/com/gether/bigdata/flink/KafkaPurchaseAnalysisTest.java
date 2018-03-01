package com.gether.bigdata.flink;

import com.gether.bigdata.flink.bean.Item;
import com.gether.bigdata.flink.bean.Order;
import com.gether.bigdata.flink.bean.OrderUser;
import com.gether.bigdata.flink.bean.OrderUserItem;
import com.gether.bigdata.flink.bean.User;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * 目前有三个表：产品表，用户表，订单表。求性别平均消费金额
 */
public class KafkaPurchaseAnalysisTest {

    public static DateTimeFormatter dataTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final int second = 30;

    public static final String TOPIC_INPUT_ORDERS = "orders1";
    public static final String TOPIC_INPUT_USERS = "users1";
    public static final String TOPIC_INPUT_ITEMS = "items1";

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);

        // get input data by connecting to the socket
        //DataStream<String> ordersStream = env.socketTextStream("localhost", 9090, "\n");
        //DataStream<String> itemsStream = env.socketTextStream("localhost", 9091, "\n");
        //DataStream<String> usersStream = env.socketTextStream("localhost", 9092, "\n");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "test");
        FlinkKafkaConsumer010<String> orderConsumer = new FlinkKafkaConsumer010<String>(TOPIC_INPUT_ORDERS, new SimpleStringSchema(), properties);
        FlinkKafkaConsumer010<String> userConsumer = new FlinkKafkaConsumer010<String>(TOPIC_INPUT_USERS, new SimpleStringSchema(), properties);
        FlinkKafkaConsumer010<String> itemsConsumer = new FlinkKafkaConsumer010<String>(TOPIC_INPUT_ITEMS, new SimpleStringSchema(), properties);
        DataStream<String> ordersStream = env.addSource(orderConsumer);
        DataStream<String> usersStream = env.addSource(userConsumer);
        DataStream<String> itemsStream = env.addSource(itemsConsumer);

        //DataStream<PurchaseAnalysis.OrderUser> ordersUserStream =
        DataStream<Tuple2<String, Double>> result = ordersStream
                .filter(s -> StringUtils.isNotBlank(s) && s.split(",").length == 4)
                .flatMap(new FlatMapFunction<String, Order>() {
                    @Override
                    public void flatMap(String s, Collector<Order> collector) throws Exception {
                        String[] split = s.split(",");
                        LocalDateTime time = LocalDateTime.parse(split[2].trim(), dataTimeFormatter);
                        long ms = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        collector.collect(new Order(split[0].trim(), split[1].trim(), ms, Integer.valueOf(split[3].trim())));
                    }
                })
                .join(
                        usersStream.filter(s -> StringUtils.isNotBlank(s) && s.split(",").length == 4)
                                .flatMap(new FlatMapFunction<String, User>() {
                                    @Override
                                    public void flatMap(String s, Collector<User> collector) throws Exception {
                                        String[] split = s.split(",");
                                        collector.collect(new User(split[0].trim(), split[1].trim(), split[2].trim(), Integer.valueOf(split[3].trim())));
                                    }
                                })
                )
                .where((KeySelector<Order, String>) order -> order.getUserName())
                .equalTo((KeySelector<User, String>) user -> user.getName())
                .window(TumblingProcessingTimeWindows.of(Time.seconds(second)))
                .apply((JoinFunction<Order, User, OrderUser>) (order, user) -> {
                    //System.out.println(LocalDateTime.now().toString() + "_" + JSON.toJSONString(order) + "_" + JSON.toJSONString(user));
                    OrderUser orderUser = OrderUser.fromOrderUser(order, user);
                    return orderUser;
                })
                .join(
                        itemsStream.filter(s -> StringUtils.isNotBlank(s) && s.split(",").length == 4)
                                .flatMap(new FlatMapFunction<String, Item>() {
                                    @Override
                                    public void flatMap(String s, Collector<Item> collector) throws Exception {
                                        String[] split = s.split(",");
                                        collector.collect(new Item(split[0].trim(), split[1].trim(), split[2].trim(), Double.valueOf(split[3].trim())));
                                    }
                                })
                )
                .where((KeySelector<OrderUser, String>) orderUser -> orderUser.getItemName())
                .equalTo((KeySelector<Item, String>) item -> item.getItemName())
                .window(TumblingProcessingTimeWindows.of(Time.seconds(second)))
                .apply((JoinFunction<OrderUser, Item, OrderUserItem>) (orderUser, item) -> {
                    //System.out.println(LocalDateTime.now().toString() + "_" + JSON.toJSONString(orderUser) + "_" + JSON.toJSONString(item));
                    return OrderUserItem.fromOrderUser(orderUser, item);
                })
                .map(new MapFunction<OrderUserItem, Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> map(OrderUserItem orderUserItem) throws Exception {
                        return new Tuple2<String, Double>(orderUserItem.getGender(), orderUserItem.getItemPrice() * orderUserItem.getQuantity());
                    }
                })
                .keyBy(0)
                .reduce(new ReduceFunction<Tuple2<String, Double>>() {
                    @Override
                    public Tuple2<String, Double> reduce(Tuple2<String, Double> stringDoubleTuple2, Tuple2<String, Double> t1) throws Exception {
                        return new Tuple2<String, Double>(stringDoubleTuple2.getField(0), ((Double) stringDoubleTuple2.getField(1)) + ((Double) t1.getField(1)));
                    }
                });

        //DataStream<PurchaseAnalysis.OrderUserItem> orderUserItemStream = ordersUserStream
        //        .join(
        //                itemsStream.filter(s -> StringUtils.isNotBlank(s) && s.split(",").length == 4)
        //                        .flatMap(new FlatMapFunction<String, Item>() {
        //                            @Override
        //                            public void flatMap(String s, Collector<Item> collector) throws Exception {
        //                                String[] split = s.split(",");
        //                                collector.collect(new Item(split[0].trim(), split[1].trim(), split[2].trim(), Double.valueOf(split[3].trim())));
        //                            }
        //                        })
        //        )
        //        .where((KeySelector<PurchaseAnalysis.OrderUser, String>) orderUser -> orderUser.getItemName())
        //        .equalTo((KeySelector<Item, String>) item -> item.getItemName())
        //        .window(TumblingProcessingTimeWindows.of(Time.seconds(second)))
        //        .apply((JoinFunction<PurchaseAnalysis.OrderUser, Item, PurchaseAnalysis.OrderUserItem>) (orderUser, item) -> {
        //            //System.out.println(LocalDateTime.now().toString() + "_" + JSON.toJSONString(orderUser) + "_" + JSON.toJSONString(item));
        //            return PurchaseAnalysis.OrderUserItem.fromOrderUser(orderUser, item);
        //        });

        //DataStream<Tuple2<String, Double>> result = orderUserItemStream
        //        .map(new MapFunction<PurchaseAnalysis.OrderUserItem, Tuple2<String, Double>>() {
        //            @Override
        //            public Tuple2<String, Double> map(PurchaseAnalysis.OrderUserItem orderUserItem) throws Exception {
        //                return new Tuple2<String, Double>(orderUserItem.getGender(), orderUserItem.getItemPrice() * orderUserItem.getQuantity());
        //            }
        //        })
        //        .keyBy(0)
        //        .reduce(new ReduceFunction<Tuple2<String, Double>>() {
        //            @Override
        //            public Tuple2<String, Double> reduce(Tuple2<String, Double> stringDoubleTuple2, Tuple2<String, Double> t1) throws Exception {
        //                return new Tuple2<String, Double>(stringDoubleTuple2.getField(0), ((Double) stringDoubleTuple2.getField(1)) + ((Double) t1.getField(1)));
        //            }
        //        });

        result.print().setParallelism(1);
        env.execute("Kafka MultiStream Join Test");
    }
}