package core.wc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {
  def main(args: Array[String]): Unit = {
    //MC TODO:建立和Spark框架的连接
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val context = new SparkContext(sparkConf)
    //MC TODO:执行业务操作
    //MC TODO:读取文件
    var lines: RDD[String] = context.textFile("F:\\大数据资料\\learning\\BigData-learning\\data\\data.txt")
    //MC TODO:将数据进行拆分
    val words = lines.flatMap(_.split(" "))
    //MC TODO:将数据根据单词进行分组 便于统计
    val wordGroup = words.groupBy(word => word)
    //MC TODO:分组后数据进行转换
    //    (hello, hello, hello), (world, world)
    //    (hello, 3), (world, 2)
    val wordToCount = wordGroup.map {
      case (word,list) => {
        (word,list.size)
      }
    }
    //MC TODO:打印
    val arrays: Array[(String, Int)] = wordToCount.collect()
    arrays.foreach(println)
    //MC TODO:关闭连接
    context.stop()
  }
}
