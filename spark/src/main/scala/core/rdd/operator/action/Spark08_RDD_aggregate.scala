package core.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark08_RDD_aggregate {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("aggregate")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 2)
    //MC TODO:aggregate : 初始值会参与分区内计算,并且和参与分区间计算
    val result: Int = rdd.aggregate(10)(_ + _, _ + _)
    //MC TODO:aggregateByKey : 初始值只会参与分区内计算
    println(result)
    sc.stop()
  }
}
