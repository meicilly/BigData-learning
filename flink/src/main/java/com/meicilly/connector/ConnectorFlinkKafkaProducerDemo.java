package com.meicilly.connector;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConnectorFlinkKafkaProducerDemo {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Order {
        private String id;
        private Integer userId;
        private Double money;
        private Long orderTime;
    }
    // TODO: 2023-04-17 自定义数据源 每隔一秒产生数据 Order泛型必须写
    private static class OrderSource extends RichParallelSourceFunction<Order>{
        // TODO: 2023-04-17 表示是否产生数据
        private boolean isRunning = true;

        // TODO: 2023-04-17 模拟产生数据
        @Override
        public void run(SourceContext sourceContext) throws Exception {
            Random random = new Random();
            while (isRunning){
                Order order = new Order(
                        UUID.randomUUID().toString(), //
                        random.nextInt(10) + 1 , //
                        (double)random.nextInt(100) ,//
                        System.currentTimeMillis()
                );
                // TODO: 2023-04-17 将数据输出
                sourceContext.collect(order);
                // TODO: 2023-04-17 每隔一秒产生数据 线程休眠一秒
                TimeUnit.SECONDS.sleep(1);
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

    /**
     * 创建子类，实现接口，对数据进行序列化操作
     */
    private static class KafkaStringSchema implements KafkaSerializationSchema<String> {
        @Override
        public ProducerRecord<byte[], byte[]> serialize(String jsonStr, @Nullable Long timestamp) {
            return new ProducerRecord<>("test01", jsonStr.getBytes());
        }
    }
    public static void main(String[] args) throws Exception {
        // TODO: 2023-04-17 执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023-04-17 设置并行度
        env.setParallelism(3);
        // TODO: 2023-04-17 数据源
        DataStreamSource<Order> orderDataStream = env.addSource(new OrderSource());
        //orderDataStream.print();
        // 将订单数据Order对象，转换为JSON字符串，存储到Kafka Topic队列
        SingleOutputStreamOperator<String> jsonDataStream = orderDataStream.map(new MapFunction<Order, String>() {
            @Override
            public String map(Order order) throws Exception {
                // 阿里巴巴库：fastJson，转换对象为json字符串
                return JSON.toJSONString(order);
            }
        });
        //  jsonDataStream.printToErr();
        // TODO: 2023-04-17 写入数据时的序列化
        KafkaSerializationSchema<String> serializationSchema = new KafkaStringSchema() ;
        // TODO: 2023-04-17 传入终端 sink
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.88.11:9092,192.168.88.12:9092,192.168.88.13:9092");
        // 4-3. 构建实例对象
        FlinkKafkaProducer<String> kafkaProducer = new FlinkKafkaProducer<String>(
                "flink-topic",
                serializationSchema,
                props,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE
        );
        // 4-4. 添加接收器
        jsonDataStream.addSink(kafkaProducer) ;
        jsonDataStream.printToErr();
        env.execute("ConnectorFlinkKafkaProducerDemo");
    }
}
