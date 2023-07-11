package com.meicilly.core.wc;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class Spark02_WordCount {
    public static void main(String[] args) {
        //MC TODO:获取程序入口
        SparkConf conf = new SparkConf().setMaster("local").setAppName("wordcount");
        JavaSparkContext context = new JavaSparkContext(conf);

        //MC TODO:读取数据
        JavaRDD<String> linesRDD = context.textFile("F:\\大数据资料\\learning\\BigData-learning\\data\\data.txt");
        //MC TODO:切割押平
        JavaRDD<String> stringJavaRDD = linesRDD.flatMap(t -> Arrays.asList(t.split(" ")).iterator());
        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = stringJavaRDD.mapToPair(t -> new Tuple2<String, Integer>(t, 1));
        JavaPairRDD<String, Integer> result = stringIntegerJavaPairRDD.reduceByKey((a, b) -> a + b);
        System.out.println(result.collect());
        context.close();
    }
}
