package com.meicilly.window.count;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

public class SlidingCountWindowDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // TODO: 2023-04-20 获取数据源
        DataStreamSource<String> inputStream = env.socketTextStream("192.168.88.11", 9999);
        // TODO: 2023-04-20 过滤数据转换
        SingleOutputStreamOperator<Integer> mapStream = inputStream.filter(line -> line.trim().length() > 0)
                .map(new MapFunction<String, Integer>() {
                    @Override
                    public Integer map(String value) throws Exception {
                        System.out.println("item:" + value);
                        return Integer.valueOf(value);
                    }
                });
        SingleOutputStreamOperator<String> windowStream = mapStream
                .countWindowAll(5,2) // TODO: 2023-04-20 设置窗口 滚动计数窗口
                // TODO: 2023-04-20 设置窗口函数 计数窗口中数据
                .apply(new AllWindowFunction<Integer, String, GlobalWindow>() {
                    @Override
                    public void apply(GlobalWindow globalWindow, Iterable<Integer> iterable, Collector<String> out) throws Exception {
                        // TODO: 2023-04-20 对窗口中的数据进行求和
                        int sum = 0;
                        for (Integer value : iterable) {
                            sum += value;
                        }
                        // 输出累计求和值
                        String output = "sum = " + sum;
                        out.collect(output);
                    }
                });
        windowStream.printToErr();
        env.execute("");
    }
}
