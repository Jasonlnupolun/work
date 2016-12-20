package com.scala.kanke.arg.canopy

import breeze.linalg.DenseVector

import scala.beans.BeanProperty

/**
  * Created by Administrator on 2016/11/21.
  */
class VideoVector(var id:String,var x:DenseVector[Double]) {
  @BeanProperty var tags:Array[String] =null
  @BeanProperty var mark: Int = VideoVector.MARK_NULL
}
object VideoVector{
  val MARK_NULL: Int = 0
  val MARK_STRONG: Int = 2
  val MARK_WEAK: Int = 1
}
