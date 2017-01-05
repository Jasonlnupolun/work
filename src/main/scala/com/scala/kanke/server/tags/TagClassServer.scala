package com.scala.kanke.server.tags

import com.google.gson.Gson
import com.java.kanke.utils.bean.{UserHistory, Video}
import com.scala.kanke.arg.canopy.{CanopyBuilder, VideoVector, Canopy}
import com.scala.kanke.arg.knn.Knn.Result
import com.scala.kanke.arg.knn.{Knn, Vectoriza, FeatureBean}
import com.scala.kanke.common.{ConfigClass, Constant}
import com.scala.kanke.service.{UserHistoryService, TagClassServiceImpl}
import org.apache.log4j.Logger

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Administrator on 2016/12/22.
  */
trait TagClassServer {
  def getOrDefault():List[Video]
  def getuserHisttory(userid:List[UserHistory]):List[FeatureBean]

}

class TagClassServerImpl extends TagClassServer{
  val log = Logger.getLogger(getClass)
  val tagclassService  = new TagClassServiceImpl
  val userHistoryService = new UserHistoryService()
  val gson = new Gson

  def getOrDefault():List[Video]={
    tagclassService.queryDefaultVideo()
  }


  def  startServerTags(userHistory:List[UserHistory]):String={
    //1，根据簇内影片数量过滤分类推荐结果
    //1.查看用户的历史 转换为向量的 bean

    val userVideolist = getuserHisttory(userHistory)
    val historyIds = userHistory.map(x=>x.getKankeid).toSet
    //2.根据用户的历史进行聚类
    val canopys = doCanopy(userVideolist)

    //3.根据聚类的中心向量进行knn  并进行去重复
    val knnResult = getKnnResult(canopys,historyIds).sortWith(_.clusterweight>_.clusterweight).take(10)
    //4.遍历所有的簇  获取影片簇的标签  设置该簇的权重和影片的数量
    var tagTemp = scala.collection.mutable.Set[String]() // 使簇内影片不重复
    var labelMap = new java.util.LinkedHashMap[String,String]() // 使簇内影片不重复
    var knndiffid = new ArrayBuffer[String]()
    for(i<-knnResult){
      val label = i.tagsString.diff(ConfigClass.labelremove).take(2).toSet
      val labeltag =label.diff(tagTemp).mkString(" ")

      // 每个聚类中心knn出的结果需要进行去重复
      if(labeltag!=""){
        tagTemp ++= label
        val labelidsArray = i.knnResult.map(x=>x.id).diff(knndiffid)
        knndiffid ++= labelidsArray
        val labelids = labelidsArray.mkString(";")
        labelMap.put(labeltag,labelids)
      }
    }
    val jsonStr = gson.toJson(labelMap);
    jsonStr
  }


  //根据聚类的中心,获取最近的数据
  case class CanopyTagData(tagsString:Array[String],knnResult:ArrayBuffer[Result],clusterweight:Double)
  def getKnnResult(canopys:ArrayBuffer[Canopy],ids:Set[String]):Seq[CanopyTagData]={
    val listKnnResult = canopys.map(k=>{
      k.computeTags() //计算下标签
      val n = k.points.size*10
      CanopyTagData(k.getTags, Knn.searchIdsByVector(k.getCenter.x,Constant.allGraph,n,ids),k.getWeight)
    })
    listKnnResult
  }

  //根据类型查看历史
  override def getuserHisttory(userHistory:List[UserHistory]):List[FeatureBean]={
    //根据用户历史  查全信息量
    val listvideo = userHistoryService.userHistoryToVideo(userHistory)
    listvideo.toList
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
