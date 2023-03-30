package com.meicilly.batch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class BatchWordCount {
    /**
     * 使用Flink计算引擎实现离线批处理：词频统计WordCount
     * 1.执行环境-env
     * 2.数据源-source
     * 3.数据转换-transformation
     * 4.数据接收器-sink
     * 5.触发执行-execute
     */
    public static void main(String[] args) throws Exception {
        // TODO: 2023-03-16 获取执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023-03-16 获取数据源
        DataSource<String> dataSource = env.readTextFile("D:\\大数据资料\\my-learning\\flink\\src\\main\\data");
        // 3.数据转换-transformation

        // TODO: 2023-03-16 分割单词
        FlatMapOperator<String, String> wordDataSet = dataSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String line, Collector<String> out) throws Exception {
                String[] words = line.trim().split("\\s");
                for (String word:words) {
                    out.collect(word);
                }
            }
        });
        // TODO: 2023-03-16 转换成二元数组
        MapOperator<String, Tuple2<String,Integer>> tupleDataSet = wordDataSet.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String,Integer> map(String word) throws Exception {
                return Tuple2.of(word,1);
            }
        });
        // TODO: 2023-03-16 分组求和
        AggregateOperator<Tuple2<String, Integer>> result = tupleDataSet.groupBy(0).sum(1);
        // TODO: 2023-03-16 接收数据处理器
        result.print();

    }
}
