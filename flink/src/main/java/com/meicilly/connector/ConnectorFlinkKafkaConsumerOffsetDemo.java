package com.meicilly.connector;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConnectorFlinkKafkaConsumerOffsetDemo {
    public static void main(String[] args) throws Exception {
        // TODO: 2023/4/16 执行环境 env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
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

        // TODO: 2023/4/16 1.Flink从topic中最初的数据开始消费
        //kafkaConsumer.setStartFromEarliest();

        // TODO: 2023/4/16 2.Flink从topic中最新的数据开始消费
        //kafkaConsumer.setStartFromLatest();

        // TODO: 2023/4/16 3.Flink从topic中指定的group上次消费的位置开始消费 所以必须配置group.id的参数
        //kafkaConsumer.setStartFromGroupOffsets();

        // TODO: 2023/4/16 4.Flink从topic中指定的offset开始
        Map<KafkaTopicPartition, Long> offsets = new HashMap<>();
        offsets.put(new KafkaTopicPartition("test01", 0), 100L);
        offsets.put(new KafkaTopicPartition("test01", 1), 90L);
        offsets.put(new KafkaTopicPartition("test01", 2), 110L);
        kafkaConsumer.setStartFromSpecificOffsets(offsets);

        // TODO: 5、指定时间戳消费数据
        kafkaConsumer.setStartFromTimestamp(1644935966961L) ;
        // 从Kafka消费数据
        DataStreamSource<String> kafkaDataStream = env.addSource(kafkaConsumer);

        // 3. 数据转换-transformation
        // 4. 数据终端-sink
        kafkaDataStream.printToErr();

        // 5. 触发执行-execute
        env.execute("ConnectorFlinkKafkaConsumerOffsetDemo") ;
    }
}
