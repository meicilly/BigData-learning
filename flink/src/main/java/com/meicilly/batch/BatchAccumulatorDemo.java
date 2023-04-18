package com.meicilly.batch;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BatchAccumulatorDemo {
    // TODO: 2023-04-18 重写map方法 实现数据的处理和计数器累加功能
    private static class CounterMapFunction extends RichMapFunction<String,String>{
        // TODO: 2023-04-18 定义累加器
        private IntCounter counter = new IntCounter();

        @Override
        public void open(Configuration parameters) throws Exception {
            // TODO: 2023-04-18 注册累加器
            getRuntimeContext().addAccumulator("counter",counter);
        }

        @Override
        public String map(String s) throws Exception {
            // TODO: 2023-04-18 使用累加器进行累加
            counter.add(1);
            return s;
        }
    }
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(10);
        DataStreamSource<String> dataStreamSource = env.readTextFile("D:\\大数据资料\\mylearn\\BigData-learning\\flink\\src\\main\\data\\click.log");
        // TODO: 2023-04-18 数据转换-transformation
        SingleOutputStreamOperator<String> map = dataStreamSource.map(new CounterMapFunction());
        // TODO: 2023-04-18 执行触发器
        JobExecutionResult batchAccumulatorDemo = env.execute("BatchAccumulatorDemo");
        // TODO: 2023-04-18 获取累加器的值
        Object counter = batchAccumulatorDemo.getAccumulatorResult("counter");
        System.out.println("counter" + counter);
    }
}
