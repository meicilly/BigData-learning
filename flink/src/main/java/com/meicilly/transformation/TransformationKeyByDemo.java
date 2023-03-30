package com.meicilly.transformation;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @meicilly
 */
public class TransformationKeyByDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023-03-29 设置并行度
        env.setParallelism(1);

        // 2. 数据源-source
        DataStreamSource<String> dataStream = env.socketTextStream("192.168.88.11", 9999);
        // 3. 数据转换-transformation
        //dataStream.printToErr();
        // TODO: 2023-03-29 去除空格号 
        SingleOutputStreamOperator<String> filter = dataStream.filter(new FilterFunction<String>() {
            @Override
            public boolean filter(String value) throws Exception {
                return value.trim().length() > 0;
            }
        });
        // TODO: 2023-03-29 分割单词
        SingleOutputStreamOperator<String> flatmap = filter.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                String[] words = value.split("\\s");
                for (String word:words) {
                    out.collect(word);
                }
            }
        });
        // TODO: 2023-03-29 转换成为二元组
        SingleOutputStreamOperator<Tuple2<String,Integer>> map = flatmap.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String,Integer> map(String value) throws Exception {
                return Tuple2.of(value,1);
            }
        });
        // TODO: 2023-03-29 使用keyBy算子进行聚合
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = map.keyBy(tuple -> tuple.f0).sum(1);
        // 4. 数据终端-sink
        sum.printToErr();
        // 5. 触发执行-execute
        env.execute("TransformationKeyByDemo");
    }

}  