package com.meicilly.batch;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @meicilly
 */
public class BatchBroadcastDemo {
	// TODO: 2023-04-18 将小数据广播到TaskManager 便于Slot中运行SubTask任务使用
	// TODO: 2023-04-18 重写map方法
	private static class BroadcastMapFunction extends RichMapFunction<Tuple3<Integer,String,Integer>,String>{
		// TODO: 2023-04-18 定义map集合 存储广播变量
		HashMap<Integer, String> stuMap = new HashMap<>();

		

		@Override
		public void open(Configuration parameters) throws Exception {
			// TODO: 2023-04-18 获取广播数据
			List<Tuple2<Integer,String>> list = getRuntimeContext().getBroadcastVariable("students");
			// TODO: 2023-04-18 将广播变量数据放到map聚合中 当处理大表时 依据key获取小表中value值
			for (Tuple2<Integer, String> tuple2 : list) {
				stuMap.put(tuple2.f0, tuple2.f1) ;
			}
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
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(1) ;

		// 2. 数据源-source
		// 大表数据
		DataSource<Tuple3<Integer, String, Integer>> scoreDataSet = env.fromCollection(
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
		// 小表数据
		DataSource<Tuple2<Integer, String>> studentDataSet = env.fromCollection(
				Arrays.asList(
						Tuple2.of(1, "张三"),
						Tuple2.of(2, "李四"),
						Tuple2.of(3, "王五")
				)
		);

		// 3. 数据转换-transformation
        /*
            使用map算子，对成绩数据集scoreDataSet中stuId转换为stuName，关联学生信息数据集studentDataSet
         */
		MapOperator<Tuple3<Integer, String, Integer>, String> resultDataSet = scoreDataSet
				.map(new BroadcastMapFunction())
				// todo: step1. 将小表数据广播出去，哪个算子使用小表，就在算子后面进行广播，必须指定名称
				.withBroadcastSet(studentDataSet, "students");

		// 4. 数据终端-sink
		resultDataSet.print();

		// 5. 触发执行-execute
		//env.execute("BatchBroadcastDemo");
	}

}

