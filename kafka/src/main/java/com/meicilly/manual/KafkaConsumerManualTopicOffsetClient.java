package com.meicilly.manual;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerManualTopicOffsetClient {
    // TODO: 2023/4/16 自动提交的规则
    // TODO: 2023/4/16 根据时间周期来提交下一次要消费的offset 记录在——consumer_offsets中 每一秒提交记录一次
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
        // TODO: 2023/4/11 读取数据对KV进行反序列化
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // TODO: 2023/4/11 构建消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // TODO: 2023/4/11 先订阅topic
        consumer.subscribe(Arrays.asList("test01"));

        // TODO: 2023/4/16 指定分区
        TopicPartition part1 = new TopicPartition("test01", 1);
        //consumer.assign(Arrays.asList(part1));
        // TODO: 2023/4/11
        while (true){
            // TODO: 2023/4/16 step1:拉去订阅的topic中的数据 100ms是一个超时时间
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            // TODO: 2023/4/16 消费每一条数据对象
            for (ConsumerRecord<String, String> record : records){
                //Topic
                String topic = record.topic();
                //part
                int part = record.partition();
                //offset
                long offset = record.offset();
                //Key And Value
                String key = record.key();
                String value = record.value();
                //模拟处理：输出
                System.out.println(topic+"\t"+part+"\t"+offset+"\t"+key+"\t"+value);
            }
            //手动提交offset
            consumer.commitSync();
        }
    }
}
