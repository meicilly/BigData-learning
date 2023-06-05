package com.flink.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkSource_ReadTextFile {
    public static void main(String[] args) throws Exception {
        // TODO: 2023-06-05 构建环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023-06-05 设置并行度
        env.setParallelism(1);
        //System.setProperty("HADOOP_USER_NAME", "bigdata");
        // TODO: 2023-06-05 数据源 如果是文件系统就是 file:/// 如果是hdfs就是hdfs://
        DataStreamSource<String> sourceText = env.readTextFile("file:///D:\\大数据资料\\mylearn\\BigData-learning\\data\\data.txt");
        sourceText.print();
        env.execute("FlinkSource_ReadTextFile");
    }
}
