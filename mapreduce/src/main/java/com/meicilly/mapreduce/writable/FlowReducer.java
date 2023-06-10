package com.meicilly.mapreduce.writable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text,FlowBean,Text,FlowBean> {
    private FlowBean outV = new FlowBean();
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Reducer<Text, FlowBean, Text, FlowBean>.Context context) throws IOException, InterruptedException {
        long totalUp = 0;
        long totalDown = 0;
        // TODO: 2023/6/10 遍历value 分别累加上行流量和下行的流量
        for (FlowBean flowBean:values) {
           totalUp +=  flowBean.getUpFlow();
           totalDown += flowBean.getDownFlow();
        }
        // TODO: 2023/6/10 封装outKV
        outV.setUpFlow(totalUp);
        outV.setDownFlow(totalDown);
        outV.setSumFlow();
        // TODO: 2023/6/10 写出outK outV
        context.write(key,outV);
    }
}
