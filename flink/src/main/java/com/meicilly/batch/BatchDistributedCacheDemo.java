package com.meicilly.batch;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @meicilly
 */
public class BatchDistributedCacheDemo {

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 数据源-source

        // 3. 数据转换-transformation

        // 4. 数据终端-sink

        // 5. 触发执行-execute
        env.execute("BatchDistributedCacheDemo");
    }

}  