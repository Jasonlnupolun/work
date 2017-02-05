package com.scala.kanke.arg.canopy

import breeze.linalg.DenseVector
import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

/**
  * Created by gavin on 15-10-23.
  * canopy可以提取均值向量,如果一个canopy含有多个向量,则id属性值为-1
  */
class Canopy {
  @BeanProperty var center: VideoVector = null
  @BeanProperty var weight: Double = 0
  @BeanProperty var tags: Array[String] = null
  @BeanProperty var idWeights: Array[(String, Double)] = null
  @BeanProperty var points: ArrayBuffer[VideoVector] = ArrayBuffer[VideoVector]()

  def computeCenter {
    var x: DenseVector[Double]=DenseVector.zeros(center.x.length)
    for (point <- getPoints) {
      x = x + point.x
    }
    val z: Double = getPoints.size
    setCenter(new VideoVector("0",x/z))
  }
  //计算簇内的标签
  def computeTags(limit:Int = 3) : Unit ={
    this.weight = points.size
     val tags =if(points.size<2){
       points(0).getTags.take(limit).groupBy(x=>x).map(x =>x._1->x._2.size).toArray
      }else{
       points.flatMap(_.getTags).groupBy(x=>x).map(x =>x._1->x._2.size).toArray.sortWith(_._2>_._2).take(limit)
     }
      if(tags.isEmpty){
        this.tags  = Array("其他")
      }else{
        this.tags= tags.map(x=>x._1)
      }
  }

  //计算簇的个数
  def computeWeight {
    setWeight(points.size)
  }

}
