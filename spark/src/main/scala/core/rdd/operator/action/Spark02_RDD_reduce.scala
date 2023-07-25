package core.rdd.operator.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark02_RDD_reduce {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("reduce")
    val context: SparkContext = new SparkContext(conf)
    //MC TODO:reduce
    val rdd: RDD[Int] = context.makeRDD(List(1, 2, 3, 4, 5))
    val i: Int = rdd.reduce(_ + _)
    println(i)

  }
}
