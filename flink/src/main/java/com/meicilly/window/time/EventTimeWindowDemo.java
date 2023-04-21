package com.meicilly.window.time;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @meicilly
 */
public class EventTimeWindowDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);

        // 2. 数据源-source
		DataStreamSource<String> inputStream = env.socketTextStream("192.168.233.11", 9999);
		// 3. 数据转换-transformation
		inputStream.filter(line -> line.trim().split(",").length == 3);

        // 4. 数据终端-sink

        // 5. 触发执行-execute
        env.execute("EventTimeWindowDemo");
    }

}  