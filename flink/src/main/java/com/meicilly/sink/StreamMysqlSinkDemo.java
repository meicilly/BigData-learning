package com.meicilly.sink;

import com.mysql.cj.jdbc.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @meicilly
 */
public class StreamMysqlSinkDemo {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Student{
		private Integer id ;
		private String name ;
		private Integer age ;
	}

	// TODO: 2023-03-23 自定义sink接收器 将DataStream写入到Mysql数据表中
	public static class  MysqlSourceSink extends RichSinkFunction<Student> {
		// TODO: 2023-03-23 定义变量
		private Connection conn = null;
		private PreparedStatement pstmt = null ;

		@Override
		public void open(Configuration parameters) throws Exception {
			// TODO: 2023-03-23 step1 加载数据
			Class.forName("com.mysql.cj.jdbc.Driver");
			// TODO: 2023-03-23 获取连接
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://192.168.88.11:3306/?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true",
					"root",
					"123456"
			);
			// TODO: 2023-03-23 创建statement对象
			pstmt = conn.prepareStatement("INSERT INTO db_flink.t_student(id, name, age) VALUES (?, ?, ?)") ;
		}
		// TODO: 2023-03-23 数据流中的每条数据输出操作

		@Override
		public void invoke(Student student, Context context) throws Exception {
			// TODO: 2023-03-23   step4、执行操作，先设置占位符值
			pstmt.setInt(1, student.id);
			pstmt.setString(2, student.name);
			pstmt.setInt(3, student.age);
			pstmt.execute();
		}

		@Override
		public void close() throws Exception {
			// TODO: 2023-03-23   step5. 关闭连接
			if(null != pstmt) {
				pstmt.close();
			}
			if(null != conn) {
				conn.close();
			}
		}
	}
    public static void main(String[] args) throws Exception {
		

		// 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1);

        // 2. 数据源-source
		DataStreamSource<Student> inputDataStream = env.fromElements(
				new Student(21, "wangwu", 20),
				new Student(22, "zhaoliu", 19),
				new Student(23, "laoda", 25),
				new Student(24, "laoer", 23),
				new Student(25, "laosan", 21)
		);

        // 3. 数据转换-transformation

        // 4. 数据终端-sink

		inputDataStream.addSink(new MysqlSourceSink()) ;

        // 5. 触发执行-execute
        env.execute("StreamMysqlSinkDemo");
    }

}  