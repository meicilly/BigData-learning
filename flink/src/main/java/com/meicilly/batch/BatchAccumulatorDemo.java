package com.meicilly.batch;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BatchAccumulatorDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        env.execute("BatchAccumulatorDemo");
    }
}
