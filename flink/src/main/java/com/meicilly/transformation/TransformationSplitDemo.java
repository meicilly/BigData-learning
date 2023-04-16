package com.meicilly.transformation;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class TransformationSplitDemo {
    public static void main(String[] args) throws Exception {
        // TODO: 2023/4/16 初始化环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023/4/16 设置并行度
        env.setParallelism(1);
        DataStreamSource<Integer> ds = env.fromElements(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // TODO: 2023/4/16 transformation 对流中的数据按照奇数和偶数拆分并选择
        OutputTag<Integer> oddTag = new OutputTag<>("奇数", TypeInformation.of(Integer.class));
        OutputTag<Integer> evenTag = new OutputTag<>("偶数", TypeInformation.of(Integer.class));
        SingleOutputStreamOperator<Integer> process = ds.process(new ProcessFunction<Integer, Integer>() {
            @Override
            public void processElement(Integer value, ProcessFunction<Integer, Integer>.Context context, Collector<Integer> collector) throws Exception {
                // TODO: 2023/4/16 out是收集完的还是放在一起的 ctx可以将数据放到不同的OutputTag
                if (value % 2 == 0) {
                    context.output(evenTag, value);
                } else {
                    context.output(oddTag, value);
                }
            }
        });
        DataStream<Integer> oddResult = process.getSideOutput(oddTag);
        DataStream<Integer> evenResult = process.getSideOutput(evenTag);

        // TODO: 2023/4/16 sink
        System.out.println(oddTag);
        System.out.println(evenTag);
        oddResult.print("奇数");
        evenResult.print("偶数");
        // TODO: 2023/4/16 excute
        env.execute();
    }
}
