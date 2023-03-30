package com.meicilly.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @meicilly
 */
public class StreamSourceOrderDemo {
	/**
	 * 每隔1秒随机生成一条订单信息(订单ID、用户ID、订单金额、时间戳)
	 * - 随机生成订单ID：UUID
	 * - 随机生成用户ID：0-2
	 * - 随机生成订单金额：0-100
	 * - 时间戳为当前系统时间：current_timestamp
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Order {
		private String id;
		private Integer userId;
		private Double money;
		private Long orderTime;
	}

	// TODO: 2023-03-23 自定义数据源 继承抽象类 并行和富有的 
	//  自定义接口有 
	// TODO: 2023-03-23 SourceFunction 非并行数据源(并行度parallelism=1)
	// TODO: 2023-03-23 RichSourceFunction 多功能非并行数据源(并行度parallelism=1) 
	// TODO: 2023-03-23 ParallelSourceFunction 并行数据源(并行度parallelism>=1)
	// TODO: 2023-03-23 RichParallelSourceFunction 多功能并行数据源(parallelism>=1)，Kafka数据源使用该接口
	public static class orderSource extends RichParallelSourceFunction<Order> {
		// TODO: 2023-03-23 用来标识是否产生数据
		private boolean isRunning = true;
		@Override
		public void run(SourceContext<Order> ctx) throws Exception {
			Random random = new Random();
			while(isRunning){
				Order order = new Order(
						UUID.randomUUID().toString(),
						random.nextInt(2),
						(double) random.nextInt(100),
						System.currentTimeMillis()
				);
				// TODO: 2023-03-23 发送交易订单数据
				ctx.collect(order);
				// TODO: 2023-03-23 每隔一秒休息一次
				TimeUnit.SECONDS.sleep(1);
			}
		}

		@Override
		public void cancel() {
			isRunning = false;
		}
	}
    public static void main(String[] args) throws Exception {
        // 1. 执行环境-env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2. 数据源-source
		DataStreamSource<Order> orderDataStreamSource = env.addSource(new orderSource());
		// 3. 数据转换-transformation

        // 4. 数据终端-sink
		orderDataStreamSource.print();
        // 5. 触发执行-execute
        env.execute("StreamSourceOrderDemo");
    }

}  