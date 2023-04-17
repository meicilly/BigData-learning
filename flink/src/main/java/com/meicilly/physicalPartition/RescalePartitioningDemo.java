package com.meicilly.physicalPartition;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

public class RescalePartitioningDemo {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());

        //Source是一个非并行的Source
        //并行度是1
        DataStreamSource<String> lines = env.socketTextStream("node01", 8888);

        //并行度2
        SingleOutputStreamOperator<String> mapped = lines.map(new RichMapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                int indexOfThisSubtask = getRuntimeContext().getIndexOfThisSubtask();
                return value + " : " + indexOfThisSubtask;
            }
        }).setParallelism(1);

        //广播，上游的算子将一个数据广播到下游所以的subtask
        DataStream<String> shuffled = mapped.rescale();

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
