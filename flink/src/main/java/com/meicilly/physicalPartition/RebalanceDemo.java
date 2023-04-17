package com.meicilly.physicalPartition;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class RebalanceDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 构建批处理运行环境
         * 使用 env.generateSequence 创建0-100的并行数据
         * 使用 filter 过滤出来 大于8 的数字
         * 使用map操作传入 RichMapFunction ，将当前子任务的ID和数字构建成一个元组
         * 在RichMapFunction中可以使用 getRuntimeContext.getIndexOfThisSubtask 获取子任务序号
         * 打印测试
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023-04-17 创建1-100的并行数据
        DataStreamSource<Long> dataSource = env.generateSequence(1, 100);
        //TODO 使用 fiter 过滤出来 大于8 的数字
        SingleOutputStreamOperator<Long> filteredDataSource = dataSource.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long aLong) throws Exception {
                return aLong > 8;
            }
        });

        //解决数据倾斜的问题
        DataStream<Long> rebalance = filteredDataSource.rebalance();

        //TODO 使用map操作传入 RichMapFunction ，将当前子任务的ID和数字构建成一个元组
        //查看92条数据分别被哪些线程处理的，可以看到每个线程处理的数据条数
        //spark中查看数据属于哪个分区使用哪个函数？mapPartitionWithIndex
        //TODO 在RichMapFunction中可以使用 getRuntimeContext.getIndexOfThisSubtask 获取子任务序号
        SingleOutputStreamOperator<Tuple2<Long, Integer>> tuple2MapOperator = rebalance.map(new RichMapFunction<Long, Tuple2<Long, Integer>>() {
            @Override
            public Tuple2<Long, Integer> map(Long aLong) throws Exception {
                return Tuple2.of(aLong, getRuntimeContext().getIndexOfThisSubtask());
            }
        });

        //TODO 打印测试
        tuple2MapOperator.print();

        env.execute();
    }

}
