package com.meicilly.connector;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

public class ConnectorFlinkKafkaConsumerDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);

        // TODO: 2023/4/16 数据源
        Properties props = new Properties();
        props.put("bootstrap.servers","192.168.233.16:9092,192.168.233.17:9092,192.168.233.18:9092");
        props.put("group.id","test");
        // TODO: 2023/4/16 构建FlinkKafkaConsumer对象
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
                "test01",
                new SimpleStringSchema(),
                props
        );
        // TODO: 2023/4/16 添加source
        DataStreamSource<String> kafkaStream = env.addSource(kafkaConsumer);
        // TODO: 2023/4/16 数据接收器 sink
        kafkaStream.print();
        env.execute();
    }
}
