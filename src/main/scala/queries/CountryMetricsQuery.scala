package queries

import java.util.Calendar

import model.{CityCountryValueSample, YearMonthCountryMetricsItem, YearMonthCountryMetricsItemParser}
import org.apache.spark.rdd.RDD
import org.apache.spark.util.StatCounter

/**
  *
  **/
object CountryMetricsQuery {
  def run(input: RDD[CityCountryValueSample]): RDD[YearMonthCountryMetricsItem] = {
    input
      .map(item => ((item.datetime.get(Calendar.YEAR), item.datetime.get(Calendar.MONTH) + 1, item.country), item.value)) // map to ((year, month, country), stat_counter)
      .aggregateByKey(StatCounter(Nil))((acc: StatCounter, value: Double) => acc.merge(value), (acc1: StatCounter, acc2: StatCounter) => acc1.merge(acc2))
      .map(item => YearMonthCountryMetricsItemParser.FromTuple((item._1, (item._2.mean, item._2.stdev, item._2.min, item._2.max)))) // map to ((year,month,country),(mean,stdev,min,max))
  }

}
