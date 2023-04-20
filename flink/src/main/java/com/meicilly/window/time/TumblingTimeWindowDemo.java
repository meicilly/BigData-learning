package com.meicilly.window.time;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;


public class TumblingTimeWindowDemo {
    public static void main(String[] args) throws Exception {
        // TODO: 2023-04-20 创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // TODO: 2023-04-20 定义数据源
        DataStreamSource<String> inputStream = env.socketTextStream("192.168.88.11", 9999);
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
        // TODO: 2023-04-20 数据的转换
        // TODO: 2023-04-20 对数据进行转换处理 过滤脏数据 解析数据封装到二元组
        SingleOutputStreamOperator<Tuple2<String, Integer>> mapStream = inputStream.filter(line -> line.trim().split(",").length == 2)
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        System.out.println("item" + value);
                        String[] array = value.trim().split(",");
                        return Tuple2.of(array[0], Integer.parseInt(array[1]));
                    }
                });
        // TODO: 2023-04-20 窗口计算 每五秒统计最近5秒各个卡口车流量
        SingleOutputStreamOperator<String> windowStream = mapStream.keyBy(tuple -> tuple.f0)
                .window(
                        TumblingProcessingTimeWindows.of(Time.seconds(5))
                )
                .apply(
                        new WindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>() {
                            // 对日期时间数据格式化
                            private FastDateFormat format = FastDateFormat
                                    .getInstance("yyyy-MM-dd HH:mm:ss");

                            @Override
                            public void apply(String key // TODO: 2023-04-20 分组key 此处卡口编号
                                    , TimeWindow window // TODO: 2023-04-20 窗口的类型
                                    , Iterable<Tuple2<String, Integer>> input, // TODO: 2023-04-20 数据放在迭代器中
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
                        }
                );
        windowStream.printToErr();
        env.execute();
    }
}
