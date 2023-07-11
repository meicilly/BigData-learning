package core.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_WordCount {
  def main(args: Array[String]): Unit = {
    //MC TODO:建立spark的连接
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(conf)

    //MC TODO:执行业务操作 读取文件
    val lines: RDD[String] = sc.textFile("F:\\大数据资料\\learning\\BigData-learning\\data\\data.txt")
    //MC TODO:将数据一行一行拆分形成一个一个单词
    val words: RDD[String] = lines.flatMap(_.split(" "))
    //MC TODO:将单词结构进行转换 方便统计
    val wordOne: RDD[(String, Int)] = words.map(word => (word, 1))
    //MC TODO:将转换后的数据进行聚合
    val wordToSum: RDD[(String, Int)] = wordOne.reduceByKey(_ + _)
    //MC TODO:  将转换结果采集到控制台打印出来
    val array: Array[(String, Int)] = wordToSum.collect()
    array.foreach(println)
    //MC TODO:关闭连接
    sc.stop()
  }
}
