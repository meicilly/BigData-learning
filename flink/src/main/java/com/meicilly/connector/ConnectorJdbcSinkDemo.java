package com.meicilly.connector;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectorJdbcSinkDemo {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Student{
        private Integer id ;
        private String name ;
        private Integer age ;
    }
    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1) ;

        // 2. 数据源-source
        DataStreamSource<Student> inputDataStream = env.fromElements(
                new Student(23, "zhaoqi", 40),
                new Student(34, "zhaoliu", 19),
                new Student(35, "wangwu", 20),
                new Student(36, "zhaoliu", 19)
        );

        // 3. 数据转换-transformation
        // 4. 数据终端-sink
        // 4-1. 创建JdbcSink实例对象，传递参数信息
        SinkFunction<Student> jdbcSink = JdbcSink.sink(
                // a. 插入语句
                "REPLACE INTO db_flink.t_student (id, name, age) VALUES (?, ?, ?)", //
                // b. 构建Statement实例对象
                new JdbcStatementBuilder<Student>() {
                    @Override
                    public void accept(PreparedStatement pstmt, Student student) throws SQLException {
                        pstmt.setInt(1, student.id);
                        pstmt.setString(2, student.name);
                        pstmt.setInt(3, student.age);
                    }
                },
                // c. 设置执行插入参数
                JdbcExecutionOptions.builder()
                        .withBatchSize(1000)
                        .withBatchIntervalMs(200)
                        .withMaxRetries(5)
                        .build(),
                // d. 设置连接MySQL数据库信息
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        .withDriverName("com.mysql.jdbc.Driver")
                        .withUrl("jdbc:mysql://node1.itcast.cn:3306/?useUnicode=true&characterEncoding=utf-8&useSSL=false")
                        .withUsername("root")
                        .withPassword("123456")
                        .build()
        );
        // 4-2. 为数据流DataStream添加Sink
        inputDataStream.addSink(jdbcSink) ;

        // 5. 触发执行-execute
        env.execute("StreamJdbcSinkDemo") ;
    }
}
