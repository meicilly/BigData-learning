package com.flink.source;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;

public class FlinkSource_FromCollection {
    public static void main(String[] args) throws Exception {
        // TODO: 2023-06-05 初始化环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        ArrayList<String> list = new ArrayList<>();
        list.add("spark");
        list.add("flink");
        list.add("hive");
        list.add("hadoop");
        list.add("mapreduce");

        DataStreamSource<String> dataStreamSource = env.fromCollection(list);
        SingleOutputStreamOperator<String> map = dataStreamSource.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                return "nx" + value;
            }
        });
        dataStreamSource.print();

        env.execute("FlinkSource_FromCollection");
    }
}
