package com.meicilly.exactly.kafka;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.util.Properties;

public class FlinkKafkaEndToEndDemo {
    // TODO: 2023-04-24 设置checkPoint的检查点
    private static void setEnvCheckpoint(StreamExecutionEnvironment env){
        // TODO: 2023-04-24 设置checkPoint的时间
        env.enableCheckpointing(1000);
        // TODO: 2023-04-24 设置后端的状态
        env.setStateBackend(new HashMapStateBackend());
        env.getCheckpointConfig().setCheckpointStorage("file:///D:/flink-checkpoints/");
        // TODO: 2023-04-24 设置两个Checkpoint 之间最少等待时间
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // TODO: 2023-04-24 设置Checkpoint时失败次数 允许失败几次
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3);
        // TODO: 2023-04-24 设置是否清理检查点 表示Cancel时是否需要保留当前的checkPoint
        env.getCheckpointConfig().enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION
        );
        // TODO: 2023-04-24 设置checkpoint执行模式为EXACTLY_ONCE
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // TODO: 2023-04-24 设置checkpoint的超时时间 如果checkpoint在60s内尚未完成说明该次Checkpoint失败丢失
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // TODO: 2023-04-24 设置同一时间有多少个checkpoint可以同时执行
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // TODO: 2023-04-24 设置重启策略
        env.setRestartStrategy(RestartStrategies.noRestart());
    }
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStream<String> inputStream = KafkaSource(env,"flink-input-topic");
        // 4. 数据终端-sink
        kafkaSink(inputStream, "flink-output-topic");

        // 5. 触发执行-execute
        env.execute("StreamExactlyOnceKafkaDemo") ;
    }

    private static DataStream<String> KafkaSource(StreamExecutionEnvironment env, String topic) {
        // TODO: 2023-04-24 设置属性
        Properties prop = new Properties();
        prop.put("bootstrap.servers","192.168.88.11:9092,192.168.88.12:9092,192.168.88.13:9092");
        prop.put("group.id","group_id_10001");
        prop.put("flink.partition-discovery.interval-milli", "10000");
        // TODO: 2023-04-24 创建Consumer对象
        FlinkKafkaConsumer<String> kafkaConsumer = new FlinkKafkaConsumer<>(
                topic,
                new SimpleStringSchema(),
                prop
        );
        kafkaConsumer.setStartFromEarliest();
        // TODO: 2023-04-24 添加数据源
        return env.addSource(kafkaConsumer);

    }
    private static void kafkaSink(DataStream<String> stream, String topic){
        // 4-1. 向Kafka写入数据时属性设置
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "node1.itcast.cn:9092,node2.itcast.cn:9092,node3.itcast.cn:9092");
        // 端到端一致性：需要指定transaction.timeout.ms(默认为1小时)的值，需要小于transaction.max.timeout.ms(默认为15分钟)
        props.setProperty("transaction.timeout.ms", 1000 * 60 * 2 + "");
        // 4-2. 写入数据时序列化
        KafkaSerializationSchema<String> kafkaSchema = new KafkaSerializationSchema<String>() {
            @Override
            public ProducerRecord<byte[], byte[]> serialize(String element, @Nullable Long timestamp) {
                return new ProducerRecord<byte[], byte[]>(topic, element.getBytes());
            }
        };
        // 4-3. 创建Producer对象
        FlinkKafkaProducer<String> producer = new FlinkKafkaProducer<String>(
                topic,
                kafkaSchema,
                props,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE
        ) ;
        // 4-4. 添加Sink
        stream.addSink(producer);
    }
}
