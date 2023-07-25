package core.rdd.operator.tranform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_RDD_map {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("map")
    val sc: SparkContext = new SparkContext(sparkConf)
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4))
    def mapFunction(num:Int): Int = {
      num *2
    }

    // val mapRDD: RDD[Int] = rdd.map(mapFunction)
    //val mapRDD: RDD[Int] = rdd.map((num:Int)=>{num*2})
    //val mapRDD: RDD[Int] = rdd.map((num:Int)=>num*2)
    //val mapRDD: RDD[Int] = rdd.map((num)=>num*2)
    //val mapRDD: RDD[Int] = rdd.map(num=>num*2)
    val mapRDD: RDD[Int] = rdd.map(_*2)
    mapRDD.collect().foreach(println)
    sc.stop()
  }
}
