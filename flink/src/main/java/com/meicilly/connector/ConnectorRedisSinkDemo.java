package com.meicilly.connector;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;

/**
 * @meicilly
 */
public class ConnectorRedisSinkDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 数据源-source
        DataStreamSource<String> inputDataStream = env.socketTextStream("192.168.88.11", 9999);

        // 3. 数据转换-transformation
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = inputDataStream.filter(line -> null != line && line.trim().length() > 0)
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] words = value.trim().split("\\W+");
                        for (String word : words) {
                            out.collect(Tuple2.of(word, 1));
                        }
                    }
                }).keyBy(tuple -> tuple.f0).sum("f1");

        // 4. 数据终端-sink
        FlinkJedisPoolConfig config = new FlinkJedisPoolConfig.Builder()
                .setHost("192.168.88.11")
                .setDatabase(0)
                .setMinIdle(3)
                .setMinIdle(8)
                .setMaxTotal(8)
                .build();
        RedisMapper<Tuple2<String, Integer>> redisMapper = new RedisMapper<Tuple2<String, Integer>>() {

            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.HSET, "flink:word:count");
            }

            @Override
            public String getKeyFromData(Tuple2<String, Integer> stringIntegerTuple2) {
                return stringIntegerTuple2.f0;
            }

            @Override
            public String getValueFromData(Tuple2<String, Integer> stringIntegerTuple2) {
                return stringIntegerTuple2.f1 + "";
            }
        };
        // 4-3. 创建RedisSink对象
        RedisSink<Tuple2<String, Integer>> redisSink = new RedisSink<Tuple2<String, Integer>>(
                config, redisMapper
        );
        sum.addSink(redisSink);

        // 5. 触发执行-execute
        env.execute("ConnectorRedisSinkDemo");
    }

}  