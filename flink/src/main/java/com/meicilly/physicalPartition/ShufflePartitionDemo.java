package com.meicilly.physicalPartition;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class ShufflePartitionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> lines = env.socketTextStream("192.168.233.16", 9999);
        SingleOutputStreamOperator<String> mapped = lines.map(new RichMapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
                return value + " : " + indexOfThisSubtask;
            }
        }).setParallelism(1);
        //shuffle
        // TODO: 2023/4/16 接收socket的单词数据 并将每个字符串均匀的随机划分到每个分区
        //DataStream<String> shuffled = mapped.shuffle();
        /// TODO: 2023/4/16 广播 将每个字符串广播到每个分区
        //广播，上游的算子将一个数据广播到下游所以的subtask
        DataStream<String> shuffled = mapped.broadcast();

        shuffled.addSink(new RichSinkFunction<String>() {
            @Override
            public void invoke(String value, Context context) throws Exception {
                int index = getRuntimeContext().getIndexOfThisSubtask();
                System.out.println(value + " -> " + index);
            }
        });
        env.execute();
    }
}
