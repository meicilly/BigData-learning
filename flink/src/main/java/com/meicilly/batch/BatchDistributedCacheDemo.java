package com.meicilly.batch;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @meicilly
 */
public class BatchDistributedCacheDemo {
    private static class CacheMapFunction extends RichMapFunction<Tuple3<Integer, String, Integer>, String>{
        // 定义Map集合，存储广播数据
        private Map<Integer, String> stuMap = new HashMap<>() ;
        @Override
        public void open(Configuration parameters) throws Exception {
            // todo step2. 获取分布式缓存的数据
            File file = getRuntimeContext().getDistributedCache().getFile("cache_students");

            // todo step3. 读取缓存文件数据   // 1,张三
            List<String> list = FileUtils.readLines(file, Charset.defaultCharset());
            for (String item : list) {
                String[] array = item.split(",");
                stuMap.put(Integer.parseInt(array[0]), array[1]) ;
            }
            System.out.println(stuMap);
        }

        @Override
        public String map(Tuple3<Integer, String, Integer> value) throws Exception {
            // value:  Tuple3.of(1, "语文", 50)
            Integer studentId = value.f0;
            String subjectName = value.f1;
            Integer studentScore = value.f2;
            // 依据学生ID获取学生名称
            String studentName = stuMap.getOrDefault(studentId, "未知");
            // 拼凑字符串并返回
            return studentName + ", " + subjectName + ", " + studentScore;
        }
    }
    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 数据源-source
        // TODO: 2023-04-18 将数据进行缓存 不能太大 属于小文件数据
        env.registerCachedFile("data/distribute_cache_student", "cache_students");
        // 2. 数据源-source，todo：从本地集合创建1个DataSet
        // 大表数据
        DataStreamSource<Tuple3<Integer, String, Integer>> source = env.fromCollection(
                Arrays.asList(
                        Tuple3.of(1, "语文", 50),
                        Tuple3.of(1, "数学", 70),
                        Tuple3.of(1, "英语", 86),
                        Tuple3.of(2, "语文", 80),
                        Tuple3.of(2, "数学", 86),
                        Tuple3.of(2, "英语", 96),
                        Tuple3.of(3, "语文", 90),
                        Tuple3.of(3, "数学", 68),
                        Tuple3.of(3, "英语", 92)
                )
        );

        // 3. 数据转换-transformation
        SingleOutputStreamOperator<String> map = source.map(new CacheMapFunction());

        // 4. 数据接收器-sink
        map.printToErr();
        // 4. 数据终端-sink

        // 5. 触发执行-execute
        env.execute("BatchDistributedCacheDemo");
    }

}  