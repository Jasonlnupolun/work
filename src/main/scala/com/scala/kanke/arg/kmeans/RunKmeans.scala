package com.scala.kanke.arg.kmeans

import java.util
import java.util.Random

import breeze.linalg.{DenseVector, sum}
import breeze.numerics.abs
import com.google.gson.Gson
import com.scala.kanke.arg.knn.{FeatureBean, Vectoriza, Knn}
import com.scala.kanke.dao.DaoImpl
import org.netlib.blas.Sasum

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

  private var clusters: Array[java.util.ArrayList[FeatureBean]] = null  //  存放聚类的结果
  private var clusterCenters: Array[FeatureBean] = null    // 聚类中心
  private var numClusters :Int =0
  private var epsilon: Double = .0   // 阀值

  private val gson = new Gson()

  def getFeatures():List[FeatureBean]={
    val features= new Vectoriza(dao.findAll,tags++regions).vectorizer()
    features
  }

  def init(k:Int):Unit={
    numClusters = k
    randomizeCenters(k, dataset)
    clusters = new Array[util.ArrayList[FeatureBean]](numClusters)
    for(i<- 0 until numClusters)
    clusters.update(i,new util.ArrayList[FeatureBean])
  }

  //随机的进行初始化中心点
  private def randomizeCenters(numClusters: Int, data: List[FeatureBean]) {
    clusterCenters = new Array[FeatureBean](numClusters)
    val rk: Random = new Random
    var i: Int = 0
    while (i < numClusters) {
      val rand = rk.nextInt(datasize)
        clusterCenters.update(i,data(rand))
        i += 1;
    }
    for(i<-clusterCenters){
      println("中心点:"+i.getTagsString)
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
      clust=0
      dist=Double.MaxValue
      newdist=0.0
    }

    for(i<-clusters){
      println("分类个数："+i.size)
    }
  }

  //计算点与中心点的距离
  def distToCenter(datum: FeatureBean, j: FeatureBean):Double={
    var sumValue: Double = 0d
    sumValue = sum(abs(datum.getTags - j.getTags))
    sumValue
  }

  // 更新中心距离
  import scala.collection.JavaConversions._
  private def calculateClusterCenters:Unit = {
    var i: Int = 0
    while (i < numClusters) {
      var sum = DenseVector.zeros[Double](dataattr)
      val cluster = clusters(i)
      for(j <- cluster){
        sum = sum + j.getTags
      }
      val clusterbean = new FeatureBean
       clusterbean.setTags(sum / (cluster.size()+1).toDouble)
      clusterCenters.update(i,clusterbean)
      i=i+1
    }
  }


  def calculateClusterVars :Unit={

  }

  //设置阀值
  def setEpsilon(epsilon: Double) {
    if (epsilon > 0) {
      this.epsilon = epsilon
    }
  }


  // 循环进行计算
  def calculateClusters: Unit = {
//    var var1: Double = Double.MaxValue
//    var var2: Double = .0
//    var delta: Double = .0
    var i :Int = 0
    do{
      divideCluster
      calculateClusterCenters
      calculateClusterVars
      i=i+1
    }while(i<100)
  }



  //计算新旧中心之间的距离，当距离小于阈值时，聚类算法结束


  def main(args: Array[String]) {  //小于阈值时，结束循环
    // 初始化k值的个数，中心点的位置
    RunKmeans.init(20)
    calculateClusters  // 聚类逻辑
    saveDataToElast       // 保存输出结果
  }

  //保存数据到elastic中
  case class ElasticBean(id:String, tags: String, videotype: String,areaname:String,year:String ,clusterid:Int)
  def saveDataToElast():Unit={
    var temp:Int=0
    for(i<- clusters){
      for(j<-i){
        val a = gson.toJson(ElasticBean(j.getKankeid,j.getTagsString,j.getVideotype,j.areaname,j.getYear.toString,temp))
        println(a)
      }
      temp = temp + 1
    }
  }

  //输出最后聚类结果
  def printCluster():Unit={
    println("类别个数"+ clusters.size)
    for(i<- clusters){
      println("类别："+i.size())
      for(j<-i){
        println(j.getTagsString)
      }
      println("                ")
      println("       ********         ")
    }
  }


}
