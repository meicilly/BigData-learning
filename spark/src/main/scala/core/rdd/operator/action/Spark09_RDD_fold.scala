package core.rdd.operator.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark09_RDD_fold {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("fold")
    val sc: SparkContext = new SparkContext(conf)
    val rdd = sc.makeRDD(List(1,2,3,4),3)
    /**
     * Aggregate the elements of each partition, and then the results for all the partitions, using a given associative function and a neutral "zero value". The function op(t1, t2) is allowed to modify t1 and return it as its result value to avoid object allocation; however, it should not modify t2.
     * This behaves somewhat differently from fold operations implemented for non-distributed collections in functional languages like Scala. This fold operation may be applied to partitions individually, and then fold those results into the final result, rather than apply the fold to each element sequentially in some defined ordering. For functions that are not commutative, the result may differ from that of a fold applied to a non-distributed collection.
     * Params:
     * zeroValue – the initial value for the accumulated result of each partition for the op operator, and also the initial value for the combine results from different partitions for the op operator - this will typically be the neutral element (e.g. Nil for list concatenation or 0 for summation)
     * op – an operator used to both accumulate results within a partition and combine results from different partitions
     *
     *
     * 使用给定的关联函数和中性“零值”聚合每个分区的元素，然后聚合所有分区的结果。允许函数op(t1,t2)修改t1并将其作为结果值返回，以避免对象分配；然而，它不应该修改t2
     * 这与Scala等函数语言中为非分布式集合实现的折叠操作的行为有些不同。这种折叠操作可以单独应用于分区，然后将这些结果折叠成最终结果，而不是按照某些定义的顺序将折叠顺序应用于每个元素。对于不可交换的函数，其结果可能与应用于非分布式集合的折叠的结果不同。
     *
     * 参数：
     *
     * zeroValue–运算运算符的每个分区的累积结果的初始值，以及运算运算符的不同分区的组合结果的初始价值-这通常是中性元素（例如，Nil表示列表串联，0表示求和）
     *
     * op–一个运算符，用于在一个分区内累积结果并组合来自不同分区的结果
     */
    val result = rdd.fold(10)(_ + _)

    println(result)

    sc.stop()
  }
}
