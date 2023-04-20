package com.meicilly.window.time;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.MapFunction;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class SlidingTimeWindowDemo {
    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 数据源-source
        DataStream<String> inputStream = env.socketTextStream("192.168.88.11", 9999);

/*
a,3
a,2
a,7
d,9
b,6
a,5
b,3
e,7
e,4
 */

        // 3. 数据转换-transformation
        // 3-1. 对数据进行转换处理：过滤脏数据，解析数据封装到二元组
        DataStream<Tuple2<String, Integer>> mapStream = inputStream
                .filter(line -> line.trim().split(",").length == 2)
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        System.out.println("item: " + value);

                        String[] array = value.trim().split(",");
                        return Tuple2.of(array[0], Integer.parseInt(array[1]));
                    }
                });

        // todo 3-2. 窗口计算， 每隔5秒统计最近10秒中各个卡口车流量
        DataStream<String> windowStream = mapStream
                // a. 设置分组key，按照卡口分组
                .keyBy(tuple -> tuple.f0)
                // b. 设置窗口：滚动时间窗口 size = 10s , slide = 5s
                .window(
                        SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5))
                )
                // c. 窗口计算，定义窗口函数
                .apply(
                        new WindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>() {
                            // 对日期时间数据格式化
                            private FastDateFormat format = FastDateFormat
                                    .getInstance("yyyy-MM-dd HH:mm:ss");

                            @Override
                            public void apply(String key,
                                              TimeWindow window,
                                              Iterable<Tuple2<String, Integer>> input,
                                              Collector<String> out) throws Exception {
                                // 获取窗口时间信息：开始时间和结束时间
                                String winStart = this.format.format(window.getStart());
                                String winEnd = this.format.format(window.getEnd());

                                // 对窗口中数据进行统计：求和
                                int sum = 0;
                                for (Tuple2<String, Integer> item : input) {
                                    sum += item.f1;
                                }

                                // 输出结果数据
                                String output = "window: [" + winStart + " ~ "
                                        + winEnd + "], " + key + " = " + sum;
                                out.collect(output);
                            }
                        });

        // 4. 数据终端-sink
        windowStream.printToErr();

        // 5. 触发执行-execute
        env.execute("SlidingTimeWindowDemo");
    }
}
