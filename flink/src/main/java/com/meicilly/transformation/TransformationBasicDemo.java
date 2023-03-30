package com.meicilly.transformation;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @meicilly
 */
public class TransformationBasicDemo {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	private static class ClickLog {
		//频道ID
		private long channelId;
		//产品的类别ID
		private long categoryId;

		//产品ID
		private long produceId;
		//用户的ID
		private long userId;
		//国家
		private String country;
		//省份
		private String province;
		//城市
		private String city;
		//网络方式
		private String network;
		//来源方式
		private String source;
		//浏览器类型
		private String browserType;
		//进入网站时间
		private Long entryTime;
		//离开网站时间
		private Long leaveTime;
	}

    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// TODO: 2023-03-24 设置并行度
		env.setParallelism(1);

        // 2. 数据源-source
		DataStream<String> dataStreamSource = env.readTextFile("D:\\大数据资料\\my-learning\\flink\\src\\main\\data\\click.log");

		// 3. 数据转换-transformation
		SingleOutputStreamOperator<ClickLog> map = dataStreamSource.map(new MapFunction<String, ClickLog>() {
			@Override
			public ClickLog map(String line) throws Exception {
				return JSON.parseObject(line, ClickLog.class);
			}
		});
		// TODO: 2023-03-29 flatmap算子
		DataStream<String> flatMapDataStream = map.flatMap(new FlatMapFunction<ClickLog, String>() {
			@Override
			public void flatMap(ClickLog clickLog, Collector<String> out) throws Exception {
				// 获取访问数据
				Long entryTime = clickLog.getEntryTime();
				// 格式一：yyyy-MM-dd-HH
				String hour = DateFormatUtils.format(entryTime, "yyyy-MM-dd-HH");
				out.collect(hour);

				// 格式二：yyyy-MM-dd
				String day = DateFormatUtils.format(entryTime, "yyyy-MM-dd");
				out.collect(day);

				// 格式三：yyyy-MM
				String month = DateFormatUtils.format(entryTime, "yyyy-MM");
				out.collect(month);
			}
		});
		// TODO: 函数三【filter函数】，过滤使用谷歌浏览器数据
		SingleOutputStreamOperator<ClickLog> filter = map.filter(new FilterFunction<ClickLog>() {
			@Override
			public boolean filter(ClickLog value) throws Exception {
				return "谷歌浏览器".equals(value.getBrowserType());
			}
		});

		//filterDataStream.printToErr();
		// 4. 数据终端-sink
		//map.printToErr();
		filter.printToErr();



		// 5. 触发执行-execute
        env.execute("TransformationBasicDemo");
    }

}  