package com.meicilly.physicalPartition;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class GlobalPartitioningDemo {
    // TODO: 2023/4/16 发送所有的数据到下游算子的第一个task（ID=0）
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> lines = env.socketTextStream("192.168.233.16", 9999);


        SingleOutputStreamOperator<String> mapped = lines.map(new RichMapFunction<String, String>() {
            @Override
            public String map(String s) throws Exception {
                int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
                return s + ":" + indexOfThisSubtask;
            }
        }).setParallelism(1);
        DataStream<String> global = mapped.global();
        global.addSink(new RichSinkFunction<String>() {
            @Override
            public void invoke(String value, Context context) throws Exception {
                int index = getRuntimeContext().getIndexOfThisSubtask();
                System.out.println(value + " -> " + index);
            }
        });
        env.execute();
    }
}
