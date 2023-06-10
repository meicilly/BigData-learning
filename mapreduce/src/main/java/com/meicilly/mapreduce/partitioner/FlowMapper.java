package com.meicilly.mapreduce.partitioner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Java类型	    Hadoop Writable类型
 * Boolean	    BooleanWritable
 * Byte	        ByteWritable
 * Int	        IntWritable
 * Float	    FloatWritable
 * Long	        LongWritable
 * Double	    DoubleWritable
 * String	    Text
 * Map	        MapWritable
 * Array	    ArrayWritable
 * Null	        NullWritable
 */
public class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
    private Text outK = new Text();
    private FlowBean outV = new FlowBean();
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlowBean>.Context context) throws IOException, InterruptedException {
        // TODO: 2023/6/10 获取一行数据转换成字符串
        String line = value.toString();
        // TODO: 2023/6/10 切割数据
        String[] split = line.split("\t");
        // TODO: 2023/6/10 获取我们想要的数据
        String phone = split[1];
        String up = split[split.length - 3];
        String down = split[split.length - 2];
        outK.set(phone);
        outV.setUpFlow(Long.parseLong(up));
        outV.setDownFlow(Long.parseLong(down));
        outV.setSumFlow();
        // TODO: 2023/6/10 封装outK outV
        context.write(outK,outV);
    }
}
