package com.meicilly.core.wc;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;


import java.util.Arrays;
import java.util.Iterator;

public class Spark01_WordCount {
    public static void main(String[] args) {
        //MC TODO:构建连接
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("WordCount");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        //MC TODO:执行业务操作
        //MC TODO: 1.读取文件
        JavaRDD<String> linesRDD = sc.textFile("F:\\大数据资料\\learning\\BigData-learning\\data\\data.txt");

        //MC TODO: 2.将一行数据进行拆分 形成一个一个的单词
        JavaRDD<String> wordFlatmap = linesRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" ")).iterator();
            }
        });
        JavaPairRDD<String, Integer> javaPairRDD = wordFlatmap.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });
        JavaPairRDD<String, Integer> result = javaPairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
            result.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple) throws Exception {
                //System.out.println(tuple);
                System.out.println(tuple._1 + " " + tuple._2);
            }
        });
        sc.close();

    }
}
