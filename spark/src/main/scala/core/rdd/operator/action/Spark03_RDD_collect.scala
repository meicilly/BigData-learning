package core.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark03_RDD_collect {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("collect")
    val sc = new SparkContext(conf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    //MC TODO:collect 方法hi将不同分区的数据按照分区顺序采集到Driver端内存中 形成数组
    val ints: Array[Int] = rdd.collect()
    println(ints.mkString(","))
  }
}
