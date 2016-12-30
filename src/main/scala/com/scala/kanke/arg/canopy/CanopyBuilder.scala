package com.scala.kanke.arg.canopy

import breeze.linalg.norm
import scala.collection.mutable.ArrayBuffer
/**
  * Created by Administrator on 2016/11/21.
  */
object CanopyBuilder {
  private var T1: Double = 0.2
  private var T2: Double = 0.3    // 三个最后标签相同的距离是0.19
  var points: ArrayBuffer[VideoVector] = null
  var canopies: ArrayBuffer[Canopy] = null

  def cosVector(a: VideoVector, b: VideoVector): Double = {
    val y = norm(a.x) * norm(b.x)
    val result = a.x.dot(b.x)
    result/y
  }
  def run {
    import scala.collection.JavaConversions._
    while (points.size > 0) {
      for (i<- points if i!=null) {
        val current: VideoVector = i
        if (canopies.size == 0) {
          val canopy: Canopy = new Canopy
          canopy.setCenter(current)
          canopy.getPoints.+= (current)
          canopies.+= (canopy)
          points.remove(0)
        } else {
          var isRemove: Boolean = false
          var index: Int = 0
          for (canopy <- canopies) {
            val center: VideoVector = canopy.getCenter
            val d: Double = cosVector(current, center)
            if (d >= T2) {
              current.setMark(VideoVector.MARK_STRONG)
              isRemove = true
            }else if (d >= T1) {
              index += 1
              current.setMark(VideoVector.MARK_WEAK)
              canopy.getPoints.add(current)
            } else if (d < T1) {
              index += 1
            }
          }
          // 该点不属于任何分类
          if (index == canopies.size) {
            val newCanopy: Canopy = new Canopy
            newCanopy.setCenter(current)
            newCanopy.getPoints.add(current)
            canopies.add(newCanopy)
            isRemove = true
          }
          if (isRemove) {
            points.remove(0)
          }
        }
      }
    }
    for (c <- canopies) {
      c.computeCenter
//      c.computeTags()
    }
  }
}
