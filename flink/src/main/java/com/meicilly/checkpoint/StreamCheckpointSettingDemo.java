package com.meicilly.checkpoint;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

public class StreamCheckpointSettingDemo {
    // TODO: 2023-04-23 自定义数据源 每隔一秒产生数据
    private static class DataSource extends RichParallelSourceFunction<String>{
        private boolean isRunning = true;

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            while (isRunning){
                // TODO: 2023-04-23 发送数据
                ctx.collect("spark flink spark");
                // TODO: 2023-04-23 每隔一秒发送数据
                TimeUnit.SECONDS.sleep(1);
            }

        }

        @Override
        public void cancel() {
            isRunning = false;
        }
    }

    public static void main(String[] args) throws Exception {
        // TODO: 2023-04-23 执行环境
        Configuration configuration = new Configuration();
        configuration.setString("rest.port", "8081");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        // TODO: 2023-04-23 设置检查点Checkpoint属性 状态保存和快照保存
        // todo: 设置检查点Checkpoint属性，状态保存和快照保存
        setEnvCheckpoint(env);

        // 2. 数据源-source
        DataStreamSource<String> dataStream = env.addSource(new DataSource());

        // 3. 数据转换-transformation
        SingleOutputStreamOperator<Tuple2<String, Integer>> outputStream = dataStream
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String value, Collector<String> out) throws Exception {
                        String[] words = value.split("\\s+");
                        for (String word : words) {
                            out.collect(word);
                        }
                    }
                })
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        return Tuple2.of(value, 1);
                    }
                })
                .keyBy(tuple -> tuple.f0).sum(1);
        // 4. 数据终端-sink
        outputStream.printToErr();

        // 5. 触发执行-execute
        env.execute("StreamCheckpointDemo");
    }
    private static void setEnvCheckpoint(StreamExecutionEnvironment env){
        // TODO: 2023-04-23 启用检查点 设置时间间隔
        env.enableCheckpointing(5000);
        // TODO: 2023-04-23 状态后端 state存储
        env.setStateBackend(new HashMapStateBackend());
        // TODO: 2023-04-23 检查存储 CheckPoint存储
        env.getCheckpointConfig().setCheckpointStorage("file:///D:\\大数据资料\\mylearn\\BigData-learning\\data");
        // todo: 设置Checkpoint检查相关属性
        // TODO: 2023-04-23  相邻两个Checkpoint间隔最小时间
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // 5. 容忍Checkpoint失败最大次数
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3);
        // 6. 当job取消，保存Checkpoint数据，默认自动删除数据
        env.getCheckpointConfig().enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION
        );
        // 7. 允许同时进行Checkpoint数目：1个
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 8. Checkpoint超时时间，如果超过时间，就表示失败
        env.getCheckpointConfig().setCheckpointTimeout(5 * 60 * 1000L);
        // 9. Checkpoint执行模式化：精确性一次语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
    }
}
