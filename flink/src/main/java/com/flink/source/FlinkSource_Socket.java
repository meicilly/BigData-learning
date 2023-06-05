package com.flink.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkSource_Socket {
    public static void main(String[] args) throws Exception {
        // TODO: 2023/6/5 初始化环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> source = env.socketTextStream("192.168.233.16", 9999);

        source.print();
        env.execute("Flink_Socket");
    }
}
