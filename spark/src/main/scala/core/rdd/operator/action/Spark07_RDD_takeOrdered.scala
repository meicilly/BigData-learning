package core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object Spark07_RDD_takeOrdered {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("takeOrdered")
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[Int] = sc.makeRDD(List(4,3,2,1))
    val ints: Array[Int] = rdd.takeOrdered(3)
    println(ints.mkString(","))
    sc.stop()
  }
}
