package com.meicilly.manual;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

public class KafkaConsumerManualPartitionOffsetClient {
    // TODO: 2023/4/16 手动提交分区offset实现
    public static void main(String[] args) {
        // TODO: 2023/4/11 配置消费者连接对象
        Properties props = new Properties();
        // TODO: 2023/4/11 指定服务端地址
        //props.setProperty("bootstrap.servers","192.168.233.16:9092,192.168.233.17:9092,192.168.233.18:9092");
        //String KAFKA_HOST = "192.168.88.11:9092,192.168.88.12:9092,192.168.88.13:9092";
        props.put("bootstrap.servers","192.168.233.16:9092,192.168.233.17:9092,192.168.233.18:9092");
        //props.put("bootstrap.servers",KAFKA_HOST);
        // TODO: 2023/4/11 指定当前消费者组
        props.setProperty("group.id","test01");
        // TODO: 2023/4/11 是否开启自动提交
        props.setProperty("enable.auto.commit", "false");
        // TODO: 2023/4/11 自动提交时间间隔
        props.setProperty("auto.commit.interval.ms", "1000");
        // TODO: 2023/4/16 自动提交时间间隔 每1s提交一次
        //  props.setProperty("auto.commit.interval.ms", "1000");
        // TODO: 2023/4/11 读取数据对KV进行反序列化
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // TODO: 2023/4/11 构建消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // TODO: 2023/4/11 先订阅topic
        consumer.subscribe(Arrays.asList("test01"));
        // TODO: 2023/4/11
        while (true){
            // TODO: 2023/4/16 拉取订阅的topic中的数据  ConsumerRecords：存储的是本次拉取的所有数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            // TODO: 2023/4/16 step1获取数据中有哪些分区 
            Set<TopicPartition> partitions = records.partitions();
            // TODO: 2023/4/16 step2 从records中取出每个分区数据
            for (TopicPartition partition:partitions) {
                // TODO: 2023/4/16 拿到每个分区对象 从records中取出当前这个分区的数据
                List<ConsumerRecord<String, String>> partData = records.records(partition);
                // TODO: 2023/4/16 基于每个分区的数据进行处理
                long offset = 0;
                for (ConsumerRecord<String, String> record : partData){
                    //Topic
                    String topic = record.topic();
                    //part
                    int part = record.partition();
                    //offset
                    offset = record.offset();
                    //Key And Value
                    String key = record.key();
                    String value = record.value();
                    //模拟处理：输出
                    System.out.println(topic+"\t"+part+"\t"+offset+"\t"+key+"\t"+value);
                }
                //分区数据处理完成，提交这个分区的offset：分区对象，这个分区提交commit offset
                Map<TopicPartition, OffsetAndMetadata> offsets = Collections.singletonMap(partition,new OffsetAndMetadata(offset+1));
                consumer.commitSync(offsets);
            }
        }
    }
}
