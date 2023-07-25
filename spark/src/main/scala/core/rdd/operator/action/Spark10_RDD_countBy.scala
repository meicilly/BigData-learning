package core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark10_RDD_countBy {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("countBy")
    val sc: SparkContext = new SparkContext(conf)
    val rdd = sc.makeRDD(List(1, 1, 1, 4))
    val intToLong: collection.Map[Int, Long] = rdd.countByValue()

    val rdd1 = sc.makeRDD(List(
      ("a", 1), ("a", 2), ("a", 3)
    ))
    val stringToLong: collection.Map[String, Long] = rdd1.countByKey()
    println(stringToLong)

    //println(intToLong)
    sc.stop()
  }
}
