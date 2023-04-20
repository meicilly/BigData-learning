package com.meicilly.window.session;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @meicilly
 */
public class TimeSessionWindowDemo {
    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 数据源-source
        DataStreamSource<String> inputStream = env.socketTextStream("192.168.88.11", 9999);

        // 3. 数据转换-transformation

        // 4. 数据终端-sink

        // 5. 触发执行-execute
        env.execute("TimeSessionWindowDemo");
    }

}  