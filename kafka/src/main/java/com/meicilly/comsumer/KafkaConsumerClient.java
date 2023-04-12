package com.meicilly.comsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerClient {
    public static void main(String[] args) {
        // TODO: 2023/4/11 配置消费者连接对象
        Properties props = new Properties();
        // TODO: 2023/4/11 指定服务端地址
        props.setProperty("bootstrap.servers","192.168.233.16:9092,192.168.233.17:9092,192.168.233.18:9092");
        // TODO: 2023/4/11 指定当前消费者组
        props.setProperty("group.id","test01");
        // TODO: 2023/4/11 是否开启自动提交
        props.setProperty("enable.auto.commit", "true");
        // TODO: 2023/4/11 自动提交时间间隔
        props.setProperty("auto.commit.interval.ms", "1000");
        // TODO: 2023/4/11 读取数据对KV进行反序列化
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // TODO: 2023/4/11 构建消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // TODO: 2023/4/11 先订阅topic
        consumer.subscribe(Arrays.asList("test01"));
        // TODO: 2023/4/11 再消费topic
        while (true){
            //ConsumerRecords：存储的是本次拉取的所有数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            //处理数据
            //ConsumerRecord：消费到的每一条数据对象
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
        }
    }
}
