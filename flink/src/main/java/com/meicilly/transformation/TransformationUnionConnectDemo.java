package com.meicilly.transformation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * @meicilly
 */
public class TransformationUnionConnectDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// TODO: 2023-03-29 设置执行并行度
		env.setParallelism(1);

        // 2. 数据源-source
		DataStream<String> dataStream01 = env.fromElements("A", "B", "C", "D");
		DataStream<String> dataStream02 = env.fromElements("aa", "bb", "cc", "dd");
		DataStream<Integer> dataStream03 = env.fromElements(1, 2, 3, 4);

        // 3. 数据转换-transformation
		// TODO: 2023-03-29 两个流进行union要求数据流类型必须相同
		DataStream<String> unionDataStream = dataStream01.union(dataStream02);
		unionDataStream.printToErr();
		// TODO: 2023-03-29 两流进行连接,connect应用场景 -> 大表与小表维度关联
		ConnectedStreams<String, Integer> connectDataStream = dataStream01.connect(dataStream03);
		// TODO: 2023-03-29 连接流中的数据必须经过处理才能够输出 <IN1,IN2,OUT>
		SingleOutputStreamOperator<String> mapDataStream = connectDataStream.map(new CoMapFunction<String, Integer, String>() {
			@Override
			public String map1(String value) throws Exception {
				return "map left ->" + value;
			}

			@Override
			public String map2(Integer value) throws Exception {
				return "map right ->" + value;
			}
		});

		// 4. 数据终端-sink
		mapDataStream.printToErr();
        // 5. 触发执行-execute
        env.execute("TransformationUnionConnectDemo");
    }

}  