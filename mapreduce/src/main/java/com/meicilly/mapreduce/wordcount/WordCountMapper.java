package com.meicilly.mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {
    Text k = new Text();
    IntWritable v = new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        // TODO: 2023/6/10 获取一行的数据
        String line = value.toString();
        // TODO: 2023/6/10 切割
        String[] words = line.split(" ");
        // TODO: 2023/6/10 输出
        for (String word:words) {
            k.set(word);
            context.write(k,v);
        }
    }
}
