package com.scala.kanke.arg.knn

import java.util

import com.scala.kanke.arg.canopy.Canopy
import com.scala.kanke.server.mix.MixMain._
import org.apache.log4j.Logger

import collection.JavaConversions._
import java.util.{ArrayList, HashMap}
import breeze.linalg.DenseVector
import com.scala.kanke.dao.Dao
import info.debatty.java.graphs._
import info.debatty.java.graphs.build.Brute
import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

object Knn {
  val logger = Logger.getLogger(getClass)
  def knnGraph(k:Int,fbeans:Seq[FeatureBean]): Graph[DenseVector[Double]] ={
    logger.info("构建的图是"+ fbeans.size)
    val nodes: ArrayList[Node[DenseVector[Double]]] = new ArrayList[Node[DenseVector[Double]]](fbeans.size)
    fbeans.map(x=>nodes.add(new Node[DenseVector[Double]](x.getKankeid, x.getTags)))
    // 2. Instantiate and configure the build algorithm
    // 构建一个算法
    val builder: Brute[DenseVector[Double]]  = new Brute[DenseVector[Double]]
    builder.setK(k)
    val dis = new Distance
    // 3. Run the algorithm and get computed graph
    // 配置相似算法进行计算
    builder.setSimilarity(dis)
    val graph:Graph[DenseVector[Double]] = builder.computeGraph(nodes)
    // Optionnallly, define a callback to get some feedback...
    builder.setCallback(new CallbackInterface() {
      def call(data: HashMap[String, AnyRef]) {
      }
    })
    graph
  }


  def knnDaoGraph(fbeans:Seq[FeatureBean],dao:Dao,table:String): Unit ={
    logger.info("构建的图是"+ fbeans.size)
    val nodes: ArrayList[Node[DenseVector[Double]]] = new ArrayList[Node[DenseVector[Double]]](fbeans.size)
    fbeans.map(x=>nodes.add(new Node[DenseVector[Double]](x.getKankeid, x.getTags)))
    // 2. Instantiate and configure the build algorithm
    // 构建一个算法
    val builder: Brute[DenseVector[Double]]  = new Brute[DenseVector[Double]]
    builder.setK(fbeans.size)
    val dis = new Distance
    // 3. Run the algorithm and get computed graph
    // 配置相似算法进行计算
    builder.setSimilarity(dis)
    val graph:Graph[DenseVector[Double]] = builder.computeGraph(nodes)
    // Optionnallly, define a callback to get some feedback...
    builder.setCallback(new CallbackInterface() {
      def call(data: HashMap[String, AnyRef]) {
        logger.info(data)
      }
    })
    for  (n <- nodes) {
      val scores = new  util.HashMap[String,Double]() //构造一个不可变的Map[String,Int]
      val nnk = graph.get(n);
      val iterator = nnk.iterator()
      var str = ""
      while(iterator.hasNext){
        val value = iterator.next()
        scores(value.node.id)=value.similarity
      }
      val result = scores.toList.sortWith(_._2>_._2)
      for((k,v)<-result){
        str = str+k+";"
//        str =str+ k+":"+v+";"
      }
      var array =new  Array[Object](2)
      array(0)= n.id
      array(1)=str
      dao.update(table,array)
    }

  }

  //查询
  def search(query: FeatureBean,graph:Graph[DenseVector[Double]],k:Int):Double={
    val nodes: Node[DenseVector[Double]]  = new Node[DenseVector[Double]](query.getKankeid,query.getTags)
    val resultset_gnss: NeighborList = graph.searchExhaustive(nodes.value, k)
    val iterator = resultset_gnss.iterator()
    var arraybuffer = new ArrayBuffer[Double](resultset_gnss.size())
    while(iterator.hasNext){
      arraybuffer.+=(iterator.next().similarity)
    }
    arraybuffer.sum
  }

  case class Result(id:String,weight:Double)
  def searchIdsByCanopy(query: Canopy,graph:Graph[DenseVector[Double]],k:Int):ArrayBuffer[Result]= {
    val nodes: Node[DenseVector[Double]] = new Node[DenseVector[Double]](query.getCenter.id, query.getCenter.x)
    val resultset_gnss: NeighborList = graph.searchExhaustive(nodes.value, k)
    val iterator = resultset_gnss.iterator()
    var resultMap = ArrayBuffer[Result]()
    while(iterator.hasNext){
      val it = iterator.next()
      if(it.node.id!=query.getCenter.id)
      resultMap+=Result(it.node.id,it.similarity)
    }
    resultMap.sortWith( _.weight >_.weight)
  }

  def searchIds(query: FeatureBean,graph:Graph[DenseVector[Double]],k:Int):ArrayBuffer[Result]= {
    val nodes: Node[DenseVector[Double]] = new Node[DenseVector[Double]](query.getKankeid, query.getTags)
    val resultset_gnss: NeighborList = graph.searchExhaustive(nodes.value, k)
    val iterator = resultset_gnss.iterator()
    var resultMap = ArrayBuffer[Result]()
    while(iterator.hasNext){
      val it = iterator.next()
      if(it.node.id!=query.getId)
        resultMap+=Result(it.node.id,it.similarity)
    }
    resultMap.sortWith( _.weight >_.weight)
  }

  def searchIdsByVector(query: DenseVector[Double],graph:Graph[DenseVector[Double]],k:Int):ArrayBuffer[Result]= {
    val nodes: Node[DenseVector[Double]] = new Node[DenseVector[Double]]("0", query)
    val resultset_gnss: NeighborList = graph.searchExhaustive(nodes.value, k)
    val iterator = resultset_gnss.iterator()
    var resultMap = ArrayBuffer[Result]()
    while(iterator.hasNext){
      val it = iterator.next()
      if(it.node.id!="0")
        resultMap+=Result(it.node.id,it.similarity)
    }
    resultMap.sortWith( _.weight >_.weight)
  }

  def searchIdsByVector(query: DenseVector[Double],graph:Graph[DenseVector[Double]],k:Int,ids:Set[String]):ArrayBuffer[Result]= {
    val nodes: Node[DenseVector[Double]] = new Node[DenseVector[Double]]("0", query)
    val resultset_gnss: NeighborList = graph.searchExhaustive(nodes.value, k)
    val iterator = resultset_gnss.iterator()
    var resultMap = ArrayBuffer[Result]()
    while(iterator.hasNext){
      val it = iterator.next()
      if(it.node.id!="0" && !ids.contains(it.node.id) )
        resultMap+=Result(it.node.id,it.similarity)
    }
    resultMap.filter(
      _.weight>0.15
    )sortWith( _.weight >_.weight)
  }
}




class FeatureBean {

  @BeanProperty var id:String=null
  @BeanProperty var kankeid:String=null
  @BeanProperty var videoid:String=null
  @BeanProperty var channelid:String=null
  @BeanProperty var tagsString:String=""
  @BeanProperty var tags:DenseVector[Double]=null
  @BeanProperty var region:DenseVector[Double]=null
  @BeanProperty var year:Double =0
  @BeanProperty var playcount:Double=0
//  @BeanProperty var mark: Int = FeatureBean.MARK_NULL
  override def toString:String={
    super.toString
  }
}



