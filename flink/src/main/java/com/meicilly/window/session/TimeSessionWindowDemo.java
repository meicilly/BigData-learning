package com.meicilly.window.session;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @meicilly
 */
public class TimeSessionWindowDemo {
    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 数据源-source
        DataStreamSource<String> inputStream = env.socketTextStream("192.168.88.11", 9999);

        // 3. 数据转换-transformation
        SingleOutputStreamOperator<Integer> mapStream = inputStream.filter(line -> line.trim().length() > 0)
                .map(new MapFunction<String, Integer>() {
                    @Override
                    public Integer map(String value) throws Exception {
                        System.out.println("item:" + value);
                        return Integer.valueOf(value);
                    }
                });
        SingleOutputStreamOperator<String> windowStream = mapStream.windowAll(
                ProcessingTimeSessionWindows.withGap(Time.seconds(5))
        ).apply(new AllWindowFunction<Integer, String, TimeWindow>() {
            private FastDateFormat format = FastDateFormat
                    .getInstance("yyyy-MM-dd HH:mm:ss");

            @Override
            public void apply(TimeWindow window, Iterable<Integer> values, Collector<String> out) throws Exception {
                // 获取窗口时间信息：开始时间和结束时间
                String winStart = this.format.format(window.getStart());
                String winEnd = this.format.format(window.getEnd());

                // 对窗口中数据进行求和
                int sum = 0;
                for (Integer value : values) {
                    sum += value;
                }

                // 输出累计求和值
                String output = "window: [" + winStart + " ~ "
                        + winEnd + "], " + "sum = " + sum;
                out.collect(output);
            }
        });

        // 4. 数据终端-sink
        windowStream.printToErr();
        // 5. 触发执行-execute
        env.execute("TimeSessionWindowDemo");
    }

}  