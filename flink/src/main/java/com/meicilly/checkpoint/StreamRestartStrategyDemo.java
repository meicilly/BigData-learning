package com.meicilly.checkpoint;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
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

public class StreamRestartStrategyDemo {
    /**
     * 自定义数据源，每隔一定时间产生字符串
     */
    private static class MySource extends RichParallelSourceFunction<String> {
        private boolean isRunning = true ;

        @Override
        public void run(SourceContext<String> ctx) throws Exception {
            int counter = 0 ;
            while (isRunning){
                // 发送数据
                ctx.collect("spark flink flink");

                // 每隔1秒发送1次数据
                TimeUnit.SECONDS.sleep(1);

                counter += 1;
                if(counter % 5 == 0){
                    throw new RuntimeException("程序出现异常啦.......................") ;
                }
            }
        }

        @Override
        public void cancel() {
            isRunning = false ;
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        Configuration configuration = new Configuration() ;
        configuration.setString("rest.port", "8081");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
        env.setParallelism(1) ;
        // todo: 设置检查点Checkpoint属性，保存状态和快照保存
        setEnvCheckpoint(env, args) ;
        // todo: 设置重启策略，默认情况下，非程序致命错误，无限重启
        env.setRestartStrategy(
                RestartStrategies.fixedDelayRestart(3, 10000)
        );

        // 2. 数据源-source
        DataStreamSource<String> inputStream = env.addSource(new MySource());

        // 3. 数据转换-transformation
        SingleOutputStreamOperator<Tuple2<String, Integer>> outputStream = inputStream
                .filter(line -> line.trim().length() > 0)
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] words = value.trim().split("\\s+");
                        for (String word : words) {
                            out.collect(Tuple2.of(word, 1));
                        }
                    }
                })
                .keyBy(tuple -> tuple.f0).sum(1);

        // 4. 数据终端-sink
        outputStream.printToErr();

        // 5. 触发执行-execute
        env.execute("StreamRestartStrategyDemo");
    }

    /**
     * Flink 流式应用，Checkpoint检查点属性设置
     */
    private static void setEnvCheckpoint(StreamExecutionEnvironment env, String[] args) {
        /* TODO： ================================== 建议必须设置 ================================== */
// 1. 设置Checkpoint-State的状态后端为FsStateBackend，本地测试时使用本地路径，集群测试时使用传入的HDFS的路径
        env.setStateBackend(new HashMapStateBackend()) ;
        if (args.length < 1) {
            env.getCheckpointConfig().setCheckpointStorage("file:///D:/ckpt");
        } else {
            // 后续集群测试时，传入参数：hdfs://node1.itcast.cn:8020/flink-checkpoints/checkpoint
            env.getCheckpointConfig().setCheckpointStorage(args[0]);
        }
/*
2. 设置Checkpoint时间间隔为1000ms，意思是做 2 个 Checkpoint 的间隔为1000ms。
Checkpoint 做的越频繁，恢复数据时就越简单，同时 Checkpoint 相应的也会有一些IO消耗。
*/
        env.enableCheckpointing(1000);// 默认情况下如果不设置时间checkpoint是没有开启的
/*
3. 设置两个Checkpoint 之间最少等待时间，如设置Checkpoint之间最少是要等 500ms
为了避免每隔1000ms做一次Checkpoint的时候，前一次太慢和后一次重叠到一起去了
如:高速公路上，每隔1s关口放行一辆车，但是规定了两车之前的最小车距为50m
*/
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
/*
4. 设置Checkpoint时失败次数，允许失败几次
 */
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(3); //

/*
5. 设置是否清理检查点,表示 Cancel 时是否需要保留当前的 Checkpoint，默认 Checkpoint会在作业被Cancel时被删除
ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION：false，当作业被取消时，保留外部的checkpoint
ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION：true,当作业被取消时，删除外部的checkpoint(默认值)
*/
        env.getCheckpointConfig().enableExternalizedCheckpoints(
                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION
        );

        /* TODO： ================================== 直接使用默认的即可 ================================== */
/*
6. 设置checkpoint的执行模式为EXACTLY_ONCE(默认)，注意:需要外部支持，如Source和Sink的支持
 */
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
/*
7. 设置checkpoint的超时时间,如果 Checkpoint在 60s内尚未完成说明该次Checkpoint失败,则丢弃。
 */
        env.getCheckpointConfig().setCheckpointTimeout(60000);
/*
8. 设置同一时间有多少个checkpoint可以同时执行
 */
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1); // 默认为1
    }
}
