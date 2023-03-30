package com.meicilly.source;

import com.mysql.cj.jdbc.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

public class StreamSourceMysqlDemo {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Student{
        private Integer id;
        private String name;
        private Integer age;
    }
    public static class MysqlSource extends RichParallelSourceFunction<Student>{
        // TODO: 2023-03-23 定义变量是否加载数据
        private boolean isRunning = true;
        // TODO: 2023-03-23 定义变量
        private Connection conn = null ;
        private PreparedStatement pstmt = null ;
        private ResultSet result = null ;
        // TODO: 2023-03-23 运行在run方法之前的操作 比如获取连接


        @Override
        public void open(Configuration parameters) throws Exception {
            // TODO: 2023-03-23 step1 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // TODO: 2023-03-23 step2 获取连接
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.88.11:3306/?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "123456"
            );
            // TODO: 2023-03-23 创建statement对象
            pstmt = conn.prepareStatement("SELECT id, name, age FROM db_flink.t_student");
        }

        @Override
        public void run(SourceContext<Student> ctx) throws Exception {
           while (isRunning){
               // TODO: 2023-03-23 执行操作
               result = pstmt.executeQuery();
               // TODO: 2023-03-23 获取数据
               while (result.next()){
                   // TODO: 2023-03-23 获取每条数据
                   int id = result.getInt("id");
                   String name = result.getString("name");
                   int age = result.getInt("age");
                   // TODO: 2023-03-23 封装数据到实体类中
                   Student student = new Student(id, name, age);
                   // TODO: 2023-03-23 发送数据到下游
                   ctx.collect(student);
               }
               // TODO: 2023-03-23 每隔五秒加载一次数据
               TimeUnit.SECONDS.sleep(5);
           }

        }
        @Override
        public void cancel() {
            isRunning = false;
        }

        // TODO: 2023-03-23 当run方法执行完以后需要释放资源
        @Override
        public void close() throws Exception {
            // step6. 关闭连接
            if(null != result) {
                result.close();
            }
            if(null != pstmt ) {
                pstmt.close();
            }
            if(null != conn) {
                conn.close();
            }
        }
    }
    // TODO: 2023-03-23 自定义数据源，从Mysql表中加载数据，并且实时增量加载，每隔五秒加载一次
    public static void main(String[] args) throws Exception {
        // TODO: 2023-03-23 执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // TODO: 2023-03-23 数据源
        DataStreamSource<Student> source = env.addSource(new MysqlSource());
        // TODO: 2023-03-23 数据输入到终端
        source.printToErr();
        env.execute("StreamSourceMysqlDemo");
    }
}
