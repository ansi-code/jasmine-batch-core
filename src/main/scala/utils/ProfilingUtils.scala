package utils

import org.apache.spark.rdd.RDD

object ProfilingUtils {
  def timeRDD[O](rdd: RDD[O], id: String): RDD[O] = {
    val start = System.nanoTime()
    rdd.first()
    val end = System.nanoTime()

    println(s"$id - ${(end - start) / 1e09} sec")
    rdd
  }
}