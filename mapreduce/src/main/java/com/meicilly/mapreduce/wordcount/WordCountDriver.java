package com.meicilly.mapreduce.wordcount;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO: 2023/6/10 获取配置信息以及获取job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        // TODO: 2023/6/10 关联Driver程序的jar
        job.setJarByClass(WordCountDriver.class);

        // TODO: 2023/6/10 关联Mapper和Reducer的jar
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // TODO: 2023/6/10 设置Mapper输出的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // TODO: 2023/6/10 设置输入和输出路径
        //FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileInputFormat.setInputPaths(job, new Path("F:\\大数据资料\\learning\\BigData-learning\\data\\data.txt"));
        //FileOutputFormat.setOutputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path("F:\\大数据资料\\learning\\BigData-learning\\data\\dataout.txt"));
        // TODO: 2023/6/10 提交job
        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}
