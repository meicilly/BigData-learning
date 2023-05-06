package com.meicilly.window.time;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Date;

/**
 * @meicilly
 */
public class EventTimeWindowDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);

        // 2. 数据源-source
		DataStreamSource<String> inputStream = env.socketTextStream("192.168.88.11", 9999);
		// 3. 数据转换-transformation
        SingleOutputStreamOperator<String> timeStream = inputStream.filter(line -> line.trim().split(",").length == 3)
                // TODO: 2023-04-23 指定事件时间字段 并且设置值为Long
                .assignTimestampsAndWatermarks(
                        // TODO: 2023-04-23 暂时不考虑乱序问题
                        WatermarkStrategy.<String>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                                // TODO: 2023-04-23 提取数据中时间字段
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<String>() {
                                            private FastDateFormat format = FastDateFormat
                                                    .getInstance("yyyy-MM-dd HH:mm:ss");

                                            @SneakyThrows
                                            @Override
                                            public long extractTimestamp(String element, long recordTimestamp) {
                                                System.out.println("element -> " + element);
                                                // TODO: 2023-04-23 分割字符串
                                                String[] array = element.split(",");
                                                // TODO: 2023-04-23 获取事件时间
                                                String eventTime = array[0];
                                                // TODO: 2023-04-23 转换格式
                                                Date eventDate = format.parse(eventTime);
                                                return eventDate.getTime();
                                            }
                                        }
                                )
                );
        /*
2022-04-01 09:00:01,a,1
2022-04-01 09:00:02,a,1
2022-04-01 09:00:05,a,1

2022-04-01 09:00:10,a,1

2022-04-01 09:00:11,a,1
2022-04-01 09:00:14,b,1
2022-04-01 09:00:15,b,1
 */
        // 3-2. 设置事假时间滚动窗口
        DataStream<String> windowStream = timeStream
                // 解析数据，提取卡口名称和流量数据，封装到二元组中
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        // element -> 2022-04-01 09:00:01,a,1
                        // 分割字符串
                        String[] array = value.split(",");
                        return Tuple2.of(array[1], Integer.parseInt(array[2]));
                    }
                })
                // a. 指定卡口编号为分组字段
                .keyBy(tuple -> tuple.f0)
                // todo step2. 设置窗口：事件时间窗口，并且是滚动窗口
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                // c. 窗口函数，对窗口中数据进行计算
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
        env.execute("EventTimeWindowDemo");
    }

}  