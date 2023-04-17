package com.meicilly.connector;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ConnectorStreamingFileSinkDemo {
    public static void main(String[] args) throws Exception {
        // TODO: 2023-04-17 执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);
        // TODO: 2023-04-17 transformation
        // TODO: 2023-04-17 设置检查点
        env.enableCheckpointing(5000);
        // TODO: 2023-04-17 数据源
        DataStreamSource<String> dataStreamSource = env.addSource(new OrderSource());
        dataStreamSource.print();
        // 4. 数据终端-sink
        StreamingFileSink<String> fileSink = StreamingFileSink
                // 4-1. 设置存储文件格式，Row行存储
                .forRowFormat(
                        new Path("datas/file-sink"), new SimpleStringEncoder<String>()
                )
                // 4-2. 设置桶分配政策,默认基于时间的分配器，每小时产生一个桶，格式如下yyyy-MM-dd--HH
                .withBucketAssigner(new DateTimeBucketAssigner<>())
                // 4-3. 设置数据文件滚动策略
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(TimeUnit.SECONDS.toMillis(5))
                                .withInactivityInterval(TimeUnit.SECONDS.toMillis(10))
                                .withMaxPartSize(2 * 1024 * 1024)
                                .build()
                )
                // 4-4. 设置文件名称
                .withOutputFileConfig(
                        OutputFileConfig.builder()
                                .withPartPrefix("itcast")
                                .withPartSuffix(".log")
                                .build()
                )
                .build();
        // 4-4. 数据流DataStream添加Sink
        dataStreamSource.addSink(fileSink).setParallelism(1);
        env.execute("ConnectorStreamingFileSinkDemo");
    }
    // TODO: 2023-04-17 自定义数据源
    public static class OrderSource extends RichParallelSourceFunction<String>{
        private boolean isRunning = true;
        private FastDateFormat format = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
        @Override
        public void run(SourceContext<String> sourceContext) throws Exception {
            Random random = new Random();
            while (true){
                // 交易订单
                long timeMillis = System.currentTimeMillis();
                String orderId = format.format(timeMillis) + (10000 + random.nextInt(10000))  ;
                String userId = "u_" + (10000 + random.nextInt(10000)) ;
                double orderMoney = new BigDecimal(random.nextDouble() * 100).setScale(2, RoundingMode.HALF_UP).doubleValue() ;
                String output = orderId + "," + userId + "," + orderMoney + "," + timeMillis ;
                System.out.println(output);
                // 输出
                sourceContext .collect(output);
                TimeUnit.MILLISECONDS.sleep(100);
            }
        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }
}
