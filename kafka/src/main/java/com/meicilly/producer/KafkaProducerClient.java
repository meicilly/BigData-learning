package com.meicilly.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerClient {
    public static void main(String[] args) {
        // TODO: 2023/4/11 构建kafka生产者连接对象
        Properties props = new Properties();
        // TODO: 2023/4/11 指定服务端的地址
        String KAFKA_HOST = "192.168.88.11:9092,192.168.88.12:9092,192.168.88.13:9092";
        //props.put("bootstrap.servers","192.168.233.16:9092,192.168.233.17:9092,192.168.233.18:9092");
        props.put("bootstrap.servers",KAFKA_HOST);
        // TODO: 2023/4/11 ack机制 + 重试机制
        /**
         * ack：数据传输的确认码 用于定义生产者如何将数据写入kafka
         * 0: 生产者发送一条数据写入kafka 不管kafka有没有写入这条数据 都直接发送下一条 【快 不安全】
         * 1： 中和性方案，生产者发送一条数据写入kafka kafka将这条数据写入对应分区的leader副本 就返回一个ack 生产者收到ack 发送下一条
         * all/-1: 生产者发送一条数据写入kafka kafka将这条数据写入对应分区Leader副本 并且等待所有ISR:Follower同步成功，就返回一个ack 生产者就收到ack 发送下一条 【安全慢】
         * 如果ack为1或者为all,生产者没有收到ack 就认为数据丢失 重新发送这条数据
         */
        props.put("acks","all");
        // TODO: 2023/4/11 指定kafka序列化的类
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // TODO: 2023/4/11 构建生产者对象
        KafkaProducer producer = new KafkaProducer<>(props);
        // TODO: 2023/4/11 调用连接对象的方法实现生产数据
        for (int i = 0; i < 10; i++) {
            // TODO: 2023/4/11 send发送对象写入Kafka：topic key value
            //producer.send(new ProducerRecord<String,String>("test01",i+"","meicilly"+i));
            // TODO: 2023/4/11 send发送对象写入kafka： topic value
            //producer.send(new ProducerRecord<String,String>("test01","meicilly"+i));
            // TODO: 2023/4/11 生产者的数据对象 Kafka：topic partition key value
            producer.send(new ProducerRecord<String,String>("test01",0,i+"","meicilly"+i));
        }
        // TODO: 2023/4/11 释放连接
        producer.close();
    }
}
