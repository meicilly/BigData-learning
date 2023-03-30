package com.meicilly.parallelism;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @meicilly
 */
public class WordCountParallelism {

    public static void main(String[] args) throws Exception {
		// TODO: 2023-03-23 构建参数解析工具
		ParameterTool parameterTool = ParameterTool.fromArgs(args);
		if(parameterTool.getNumberOfParameters() != 2){
            System.out.println("Usage: WordCount --host <host> --port <port> ............");
            System.exit(-1);
        }
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port", 9999);
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023-03-23 执行环境级别的并行度
        env.setParallelism(3);

        // 2. 数据源-source
        DataStreamSource<String> inputStream = env.socketTextStream(host, port);
        // 3. 数据转换-transformation
        // TODO: 2023-03-23 flatmap 
        SingleOutputStreamOperator<String> wordDataStream = inputStream.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] words = s.split("\\s");
                for (String word : words) {
                    collector.collect(word);
                }
            }
        });
        // TODO: 2023-03-23 转换成二元组 
        SingleOutputStreamOperator<Tuple2<String, Integer>> tupleDataStream = wordDataStream.map(new MapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                return Tuple2.of(s, 1);
            }
        });
        // TODO: 2023-03-23  按照单词分组并求和
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = tupleDataStream.keyBy(0).sum(1);
        // 4. 数据终端-sink
        sum.print().setParallelism(1);

        // 5. 触发执行-execute
        env.execute("WordCountParallelism");
    }

}  