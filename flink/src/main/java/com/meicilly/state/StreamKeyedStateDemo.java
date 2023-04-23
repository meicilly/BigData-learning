package com.meicilly.state;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @meicilly
 */
public class StreamKeyedStateDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);
        // 2. 数据源-source
		DataStreamSource<Tuple3<String, String, Long>> tupleStream = env.fromElements(
				Tuple3.of("上海", "普陀区", 488L),
				Tuple3.of("上海", "徐汇区", 212L),
				Tuple3.of("北京", "西城区", 823L),
				Tuple3.of("北京", "海淀区", 234L),
				Tuple3.of("上海", "杨浦区", 888L),
				Tuple3.of("上海", "浦东新区", 666L),
				Tuple3.of("北京", "东城区", 323L),
				Tuple3.of("上海", "黄浦区", 111L)
		);
        // 3. 数据转换-transformation
		SingleOutputStreamOperator<Tuple3<String, String, Long>> maxStream = tupleStream.keyBy(value -> value.f0).max(2);
		// 4. 数据终端-sink
		//maxStream.printToErr();
		// todo: 自定义状态，实现max算子获取最大值，此处KeyedState定义
		SingleOutputStreamOperator<String> statStream = tupleStream
				// 指定城市字段进行分组
				.keyBy(tuple -> tuple.f0)
				// 处理流中每条数据
				.map(new RichMapFunction<Tuple3<String, String, Long>, String>() {

					// todo: 第1步、定义变量，存储每个Key对应值，
					// 所有状态State实例化都是RuntimeContext实例化
					private ValueState<Long> maxState = null ;

					// 处理流中每条数据之前，初始化准备工作
					@Override
					public void open(Configuration parameters) throws Exception {
						// todo: 第2步、初始化状态，开始默认值null
						maxState = getRuntimeContext().getState(
								new ValueStateDescriptor<Long>("maxState", Long.class)
						);
					}

					@Override
					public String map(Tuple3<String, String, Long> value) throws Exception {
						// 获取流中数据对应值
						Long currentValue = value.f2;

						// todo: step3、从状态中获取存储key以前值
						Long historyValue = maxState.value();

						// 如果数据为key分组中第一条数据；没有状态，值为null
						if(null == historyValue ||historyValue < currentValue){
							// todo: step4、更新状态值
							maxState.update(currentValue);
						}

						// 返回状态的最大值
						return value.f0 + " -> " + maxState.value();
					}
				});

		// 4. 数据终端-sink
		statStream.printToErr();
        // 5. 触发执行-execute
        env.execute("StreamKeyedStateDemo");
    }

}  