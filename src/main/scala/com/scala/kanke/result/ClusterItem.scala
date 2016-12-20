package com.scala.kanke.result

import com.java.kanke.utils.bean.Video

import scala.beans.BeanProperty
import scala.collection.JavaConversions
import scala.collection.mutable.ArrayBuffer

/**
  * Created by gavin on 16-3-8.
  */
class ClusterItem() {
  @BeanProperty var weight: Double = 0
  @BeanProperty var tags: Array[String] = null
  @BeanProperty var idWeights: Array[(Long, Double)] = null
  @BeanProperty var sourceIds: Array[Long] = null
  @BeanProperty var maxTime:Long=0
  @BeanProperty var sourceVideos:Seq[Video]=null
  def clean(oldIds:Set[Long]): Unit ={
    var buf:ArrayBuffer[(Long,Double)]=ArrayBuffer[(Long,Double)]()
    idWeights.foreach(tp=>{
      if(!oldIds.contains(tp._1)){
        buf+=tp
      }
    })
    idWeights=buf.sortWith(_._2>_._2).toArray
  }


//  def initTags(vec: Array[Double], allTags: Array[String], limit: Double, allVideos: Map[SVideoType.Value, Map[Long, MetaVideo]]): Unit = {
//    val removeIdVec = vec.splitAt(3)._2.splitAt(allTags.length)._1
//    val maxIndex = removeIdVec.indexOf(removeIdVec.max)
//    val maxTag = allTags(maxIndex)
//    val bestIndex = removeIdVec.indices.zip(removeIdVec).filter(_._2 > limit).sortWith(_._2 > _._2)
//    val besTags = bestIndex.map(tp=>allTags(tp._1))
//    if(besTags.isEmpty){
//      this.tags=Array(maxTag)
//    }else{
//      this.tags=besTags.take(Constant.conf.clusterMaxTagSize).toArray
//    }
//  }

//  /**
//    * 以交集方式处理标签
// *
//    * @param videos　簇内影片
//    * @param limit　数量阈值
//    */
//  def initTags(videos:Seq[MetaVideo],limit:Int = 4): Unit ={
//    sourceVideos = videos
//    val tags = videos.map(_.getSplitedTags)
//    val commonTag = tags.reduceLeft((b,s)=>{
//      b.filter(s.toSet.contains(_))
//    }).take(limit)
//    if(commonTag.isEmpty){
//      this.tags = Array("其他")
//    }else{
//      this.tags= commonTag
//    }
//  }
//
//  /**
//    * 处理冲突标签
//    */
//  def handleConflictTag(): Unit ={
//    tags=tags++ sourceVideos.toArray.map(_.getOrigion).toSet
//  }
}
case class JsonClusterItem(val videoTags:java.util.List[String], val videoIds:java.util.List[Long], val weight:Double)
object JsonClusterItem{
  def apply(videoTags:Seq[String],videoIds:Seq[Long],weight:Double): JsonClusterItem ={
    import JavaConversions._
    new JsonClusterItem(videoTags,videoIds,weight)
  }
}
case class JsonClusterResult(val userId:Long,val results:java.util.Map[String,java.util.List[JsonClusterItem]],val timeStamp:String)
