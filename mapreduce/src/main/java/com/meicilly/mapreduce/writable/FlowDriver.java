package com.meicilly.mapreduce.writable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import java.io.IOException;

public class FlowDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // TODO: 2023/6/10 获取job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // TODO: 2023/6/10 关联Driver类
        job.setJarByClass(FlowDriver.class);

        // TODO: 2023/6/10 关联Mapper和Reducer
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);

        // TODO: 2023/6/10 设置Map端输出kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        // TODO: 2023/6/10 设置终端输出kv
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //6 设置程序的输入输出路径
        FileInputFormat.setInputPaths(job, new Path("F:\\大数据资料\\learning\\BigData-learning\\data\\phone.txt"));
        FileOutputFormat.setOutputPath(job, new Path("F:\\大数据资料\\learning\\BigData-learning\\data\\outphone"));

//7 提交Job
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
