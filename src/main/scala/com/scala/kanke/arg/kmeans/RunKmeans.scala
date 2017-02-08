package com.scala.kanke.arg.kmeans

import java.util.Random

import breeze.linalg.{DenseVector, sum}
import com.scala.kanke.arg.knn.{FeatureBean, Vectoriza, Knn}
import com.scala.kanke.dao.DaoImpl

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Administrator on 2017/2/7.
  */
object RunKmeans {




  //读去数据，配置初始化数据
  val dao = new DaoImpl
  val tags = dao.querytags("")
  val regions = dao.queryOrgion("")

  val  dataset = getFeatures()
  val datasize = getFeatures().size
  val dataattr = tags ++ regions length

  private var clusters: ArrayBuffer[java.util.ArrayList[FeatureBean]] = null  //  存放聚类的结果
  private var clusterCenters: ArrayBuffer[FeatureBean] = null    // 聚类中心
  private var numClusters :Int =0
  private var epsilon: Double = .0   // 阀值

  def getFeatures():List[FeatureBean]={
    val features= new Vectoriza(dao.findAll,tags++regions).vectorizer()
    features
  }

  def init(k:Int):Unit={
    numClusters = k
    randomizeCenters(k, dataset)
  }

  //随机的进行初始化中心点
  private def randomizeCenters(numClusters: Int, data: List[FeatureBean]) {
    val rk: Random = new Random
    var i: Int = 0
    while (i < numClusters) {
      val rand = rk.nextInt(datasize)
        clusterCenters(i) = data(rand)
        i += 1;
    }
  }


  // 划分簇
  def divideCluster: Unit ={
    var k: Int = 0
    while (k < numClusters) {
      clusters(k).clear
      k += 1
    }
    var clust: Int = 0
    var dist: Double = Double.MaxValue
    var newdist: Double = 0
    // 从所有数据中分发到类别当中
    for (i <-0 until datasize) {
      for (  j<- 0 until numClusters ){
        newdist = distToCenter(dataset(i), clusterCenters(j))    // 计算类别的中心与整体数据的距离
        if (newdist <= dist) {
          clust = j
          dist = newdist
        }
      }
      clusters(clust).add(dataset(i))
    }
  }

  //计算点与中心点的距离
  def distToCenter(datum: FeatureBean, j: FeatureBean):Double={
    var sumValue: Double = 0d
    sumValue = sum(datum.getTags - j.getTags)
    Math.sqrt(sumValue)
  }

  // 更新中心距离
  import scala.collection.JavaConversions._
  private def calculateClusterCenters:Unit = {
    var sum = DenseVector.zeros[Double](dataattr)
    var i: Int = 0
    while (i < numClusters) {
      val cluster = clusters(i)
      for(j <- cluster){
        sum = sum + cluster.get(i).getTags
      }
      val clusterbean = new FeatureBean
       clusterbean.setTags(sum / cluster.size().toDouble)
      clusterCenters(i)= clusterbean
      i=i+1
    }
  }


  //设置阀值
  def setEpsilon(epsilon: Double) {
    if (epsilon > 0) {
      this.epsilon = epsilon
    }
  }

  def main(args: Array[String]) {

  }


}
