package com.meicilly.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @meicilly
 */
public class StreamSourceFileDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 数据源-source
        DataStreamSource<String> dataStream01 = env.readTextFile("D:\\大数据资料\\my-learning\\flink\\src\\main\\data\\words.txt");
        dataStream01.print();
        // TODO: 2023-03-23 读取压缩文件
        DataStreamSource<String> dataStream02 = env.readTextFile("datas/words.txt.gz");
        dataStream02.print();

        // 3. 数据转换-transformation

        // 4. 数据终端-sink

        // 5. 触发执行-execute
        env.execute("StreamSourceFileDemo");
    }

}  