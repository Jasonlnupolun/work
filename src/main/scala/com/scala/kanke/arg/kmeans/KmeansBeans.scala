package com.scala.kanke.arg.kmeans

import com.scala.kanke.arg.knn.FeatureBean

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

/**
  * Created by Administrator on 2017/2/8.
  */
class KmeansBeans {
  @BeanProperty var id:String =null
  @BeanProperty var clusters: java.util.ArrayList[FeatureBean] = null
  @BeanProperty var clusterCenters: FeatureBean = null
}
