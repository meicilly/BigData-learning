package core.rdd.operator.tranform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark04_RDD_flatMap_test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("flatmap_test")
    val sc = new SparkContext(sparkConf)

    // TODO 算子 - flatMap
    val rdd: RDD[String] = sc.makeRDD(List(
      "Hello Scala", "Hello Spark"
    ))

    val flatRDD: RDD[String] = rdd.flatMap(
      s => {
        s.split(" ")
      }
    )
    flatRDD.collect().foreach(println)


    sc.stop()

  }
}
