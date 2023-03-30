package com.meicilly.transformation;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @meicilly
 */
public class TransformationMaxMinDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// TODO: 2023-03-29 设置并行度
		env.setParallelism(1);

        // 2. 数据源-source
		DataStream<Tuple3<String, String, Integer>> inputDataStream = env.fromElements(
				Tuple3.of("上海", "浦东新区", 777),
				Tuple3.of("上海", "闵行区", 999),
				Tuple3.of("上海", "杨浦区", 666),
				Tuple3.of("北京", "东城区", 567),
				Tuple3.of("北京", "西城区", 987),
				Tuple3.of("上海", "静安区", 888),
				Tuple3.of("北京", "海淀区", 99)
		);

		// 3. 数据转换-transformation
		// TODO: 2023-03-29 max输出最大值
		SingleOutputStreamOperator<Tuple3<String, String, Integer>> max = inputDataStream.keyBy(tuple -> tuple.f0)
				.max(2);
		max.printToErr();

		// 4. 数据终端-sink

        // 5. 触发执行-execute
        env.execute("TransformationMaxMinDemo");
    }

}  