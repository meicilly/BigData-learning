package com.meicilly.transformation;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @meicilly
 */
public class TransformationSideOutputsDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// TODO: 2023-03-30 设置并行度
		env.setParallelism(1);
        // 2. 数据源-source
		DataStreamSource<Integer> streamSource = env.fromElements(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		// 3. 数据转换-transformation
		// TODO: 2023-03-30 对数据流进行分割 使用sideOutput侧边输出算子实现 将基数放在一个流 将偶数数字放在一个流
        // TODO: 2023-03-30 原来数据流中数据平方处理
        // TODO: 2023-03-30 定义流中分割标签
        OutputTag<Integer> oddTag = new OutputTag<Integer>("side-odd") {};
        OutputTag<Integer> evenTag = new OutputTag<Integer>("side-even"){};
        // TODO: 2023-03-30 调用process函数对流中的数据进行打标签
        SingleOutputStreamOperator<String> process = streamSource.process(new ProcessFunction<Integer, String>() {
            @Override
            public void processElement(Integer value, ProcessFunction<Integer, String>.Context ctx, Collector<String> out) throws Exception {
                // TODO: 2023-03-30 对流中的数据的计算
                int squareValue = value * value;
                out.collect(squareValue + "");
                // TODO: 2023-03-30 判断是奇数还是偶数对其打上标签
                if(value % 2 == 0){
                    ctx.output(evenTag,value);
                }else {
                    ctx.output(oddTag,value);
                }
            }
        });
        // 4. 数据终端-sink
        process.printToErr();
        // TODO: 2023-03-30 获取数据流中的标签
        DataStream<Integer> oddStream = process.getSideOutput(oddTag);
        oddStream.print("odd>");

        DataStream<Integer> evenStream = process.getSideOutput(evenTag);
        evenStream.print("even>");
        // 5. 触发执行-execute
        env.execute("TransformationSideOutputsDemo");
    }

}  