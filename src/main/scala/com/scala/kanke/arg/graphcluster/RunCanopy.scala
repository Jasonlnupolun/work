package com.scala.kanke.arg.graphcluster

import com.scala.kanke.arg.canopy.{CanopyBuilder, VideoVector, Canopy}
import com.scala.kanke.arg.knn.{Vectoriza, FeatureBean}

import scala.collection.mutable.ArrayBuffer


/**
  * Created by Administrator on 2017/1/10.
  */
object RunCanopy {
import scala.collection.JavaConversions._
  def main(args: Array[String]) {
    val canopys =doCanopy(BruteGraph.initVector().toList)
    for(i<-canopys) {
      print(i.idWeights)
    }
  }
  def doCanopy(featureBeans:List[FeatureBean]):ArrayBuffer[Canopy]={
    var pointsBuffer = ArrayBuffer[VideoVector]()
    val buffer =for (i <- featureBeans if i!=null){
      pointsBuffer += Vectoriza.featureBeanToVideoVector(i)
    }
    CanopyBuilder.points = pointsBuffer
    CanopyBuilder.canopies =ArrayBuffer[Canopy]()
    CanopyBuilder.run
    CanopyBuilder.canopies
  }
}
