package com.meicilly.connector;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ConnectorKafkaSourceDemo {
    public static void main(String[] args) throws Exception {
        // TODO: 2023/4/16 执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);

        // TODO: 2023/4/16 source源
        // TODO: 2023/4/16 创建KafkaSource对象 设置属性
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.233.16:9092,192.168.233.17:9092,192.168.233.18:9092")
                .setTopics("test01")
                .setGroupId("test")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        // TODO: 2023/4/16 添加数据源
        DataStream<String> kafkaStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "KafkaSource");
        // 3. 数据转换-transformation
        // 4. 数据接收器-sink
        kafkaStream.printToErr();

        // 5. 触发执行-execute
        env.execute("StreamKafkaSourceDemo") ;
    }
}
