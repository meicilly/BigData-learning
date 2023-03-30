package com.meicilly.transformation;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @meicilly
 */
public class TransformationReduceDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 数据源-source
		DataStreamSource<String> streamSource = env.socketTextStream("192.168.88.11", 9999);

		// 3. 数据转换-transformation
		// TODO: 2023-03-29 过滤脏数据
		SingleOutputStreamOperator<Tuple2<String, Integer>> streamOperator = streamSource.filter(line -> line.trim().length() > 0)
				.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
					@Override
					public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
						// TODO: 2023-03-29 分割单词
						String[] words = value.split("\\s");
						for (String word:words) {
							out.collect(Tuple2.of(word,1));
						}
					}
				});
		// TODO: 2023-03-29 使用keyBy算子分组后数据进行聚合操作
		SingleOutputStreamOperator<Tuple2<String, Integer>> reduce = streamOperator.keyBy(tuple -> tuple.f0)
				.reduce(new ReduceFunction<Tuple2<String, Integer>>() {
					@Override
					public Tuple2<String, Integer> reduce(Tuple2<String, Integer> tmp, Tuple2<String, Integer> item) throws Exception {
						// TODO: 2023-03-29 打印tmp和item的值
						System.out.println("tmp" + tmp + "item" + item);
						// TODO: 2023-03-29 获取当前值
						Integer historyValue = tmp.f1;
						// TODO: 2023-03-29 获取现在传递的值
						Integer currentValue = item.f1;
						// TODO: 2023-03-29 计算最新的值
						Integer lastValue = historyValue + currentValue;
						return Tuple2.of(tmp.f0,lastValue);
					}
				});

		// 4. 数据终端-sink
		reduce.printToErr();

        // 5. 触发执行-execute
        env.execute("TransformationReduceDemo");
    }

}  