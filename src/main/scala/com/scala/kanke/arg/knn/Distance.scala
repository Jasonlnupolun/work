package com.scala.kanke.arg.knn

import breeze.linalg.{DenseVector, norm}
import info.debatty.java.graphs.SimilarityInterface


/**
  * Created by Administrator on 2016/11/7.
  */
class Distance extends SimilarityInterface[DenseVector[Double]]{
  override def similarity(value1: DenseVector[Double], value2: DenseVector[Double]): Double ={
    val y = norm(value1) * norm(value2)
    val result = value1.dot(value2)
    result/y
  }

}
