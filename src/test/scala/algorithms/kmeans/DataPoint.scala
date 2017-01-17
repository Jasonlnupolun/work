package algorithms.kmeans

/**
  * Created by Administrator on 2017/1/17.
  */


object Distance {
  def manhattan[T <: Double](dataPoint: DataPoint[T], otherDataPoint: DataPoint[T]): Double = {
    dataPoint.values.foldLeft(0.0)((sum, x) => sum + Math.abs(x - otherDataPoint(dataPoint.values.indexOf(x))))
  }

  def euclidean[T <: Double](dataPoint: DataPoint[T], otherDataPoint: DataPoint[T]): Double = {
    val sqrDistance = dataPoint.values.foldLeft(0.0)((sum, x) => {
      val diff = x - otherDataPoint(dataPoint.values.indexOf(x))
      sum + diff * diff
    })
    Math.sqrt(sqrDistance)
  }


  def exponential[T <: Double](dataPoint: DataPoint[T], otherDataPoint: DataPoint[T]): Double = {
    val expDistance = dataPoint.values.foldLeft(0.0)((sum, x) => sum + Math.exp(Math.abs(x - otherDataPoint(dataPoint.values.indexOf(x)))))
    Math.log(expDistance)
  }

}


class DataPoint[T <: Double](val values: Array[T],
                             val index: Int = -1) {
  import scala.collection.mutable.ArraySeq
  @inline
  final def size: Int = values.size
  def apply(varIndex: Int): T = {
    require(varIndex >= 0 && varIndex < values.size, "Incorrect variable index for data point")
    values(varIndex)
  }
  /**
    * <p>Normalize the model features (or parameters) against the maximum
    * value for each of the feature or model parameters.</p>
    *
    * @param maxValues array of maximum values for the model features.
    */
  def normalize(maxValues: Array[T]): ArraySeq[T] =
    values map { x => (x / maxValues(values.indexOf(x))).asInstanceOf[T] }
  /**
    * <p>Display the ordered list of data points.</p>
    *
    * @return characters string of floating point values.
    */
  override def toString: String = {
    val buf = new StringBuilder(index)
    var k = 0
    while (k < values.size) {
      buf.append(" ")
      buf.append(values.apply(k))
      k += 1
    }
    buf.toString
  }

}