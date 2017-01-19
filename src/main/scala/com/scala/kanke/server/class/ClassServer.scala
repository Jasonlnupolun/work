package com.scala.kanke.server
import com.google.gson.Gson
import com.java.kanke.utils.bean.{Video, UserHistory}
import com.scala.kanke.arg.canopy.{VideoVector, CanopyBuilder, Canopy}
import com.scala.kanke.arg.knn.Knn.Result
import com.scala.kanke.arg.knn.{Knn, FeatureBean, Vectoriza}
import com.scala.kanke.common.{ClassConstant, ConfigClass, Constant}
import com.scala.kanke.service.{ClassServiceImpl, UserHistoryService}
import org.apache.log4j.Logger
import scala.collection.mutable.ArrayBuffer
/**
  * Created by Administrator on 2016/11/21.
  */
trait ClassServer{
  def getuserHisttory(userid:List[UserHistory]):List[FeatureBean]
}
class ClassServerImpl extends  ClassServer{
  val logger = Logger.getLogger(getClass)
  val userHistoryService = new UserHistoryService()
  val classService  = new ClassServiceImpl
  val gson = new Gson
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

  def startServerClass(userHistory:List[UserHistory],default:List[Video],typename:String):String={
    //1，根据簇内影片数量过滤分类推荐结果
    //1.查看用户的历史 转换为向量的 bean
    val userVideolist = getuserHisttory(userHistory)
    val historyIds = userHistory.map(x=>x.getKankeid).toSet
    //2.根据用户的历史进行聚类
    val canopys = doCanopy(userVideolist)
    //3.根据聚类的中心向量进行knn  并进行去重复
    val knnResult = getKnnResult(canopys,historyIds,typename).sortWith(_.clusterweight>_.clusterweight).take(5)
    //4.遍历所有的簇  获取影片簇的标签  设置该簇的权重和影片的数量
    var tagTemp = scala.collection.mutable.Set[String]() // 使簇内影片不重复
    var labelMap = new java.util.LinkedHashMap[String,String]() // 使簇内影片不重复
    var knndiffid = new ArrayBuffer[String]()
    for(i<-knnResult){
      val label = i.tagsString.diff(ConfigClass.labelremove).take(3).toSet
        val labeltag =label.diff(tagTemp).mkString(" ")
        if(labeltag!=""){
          tagTemp ++= label
          val labelidsArray = i.knnResult.map(x=>x.id).diff(knndiffid)
          knndiffid ++= labelidsArray
          val labelids = labelidsArray.mkString(";")
          labelMap.put(labeltag,labelids)
        }

    }
    //5.推荐几部影片,确保数据是两个标签 指定个数
    if(labelMap.size!=5){
      if(labelMap.size>0){
        labelMap.put("其他",default.take(5).map(x=>x.getKankeid).mkString(";"))
      }else{
        val hot = default.take(5)
        val other = default.slice(5,10)
        if(hot.nonEmpty){
          labelMap.put("热门",hot.map(x=>x.getKankeid).mkString(";"))
        }
        if(other.nonEmpty){
          labelMap.put("其他",other.map(x=>x.getKankeid).mkString(";"))
        }

      }
    }
    //6.返回影片类型- 返回类型标签 和 推荐ids
    writeToRedis(labelMap)
    val jsonStr = gson.toJson(labelMap);
    jsonStr
  }

  // 除去历史记录中已经看过的记录
  def cleanSeenOfHistory():Unit={

  }

  //存在返回相应的聚类，不存在返回默认的推荐结果
  def getOrDefault(typename:String):List[Video]={
    classService.queryDefaultVideo(typename)
  }

  //根据聚类的中心,获取最近的数据
  case class CanopyTagData(tagsString:Array[String],knnResult:ArrayBuffer[Result],clusterweight:Double)
  def getKnnResult(canopys:ArrayBuffer[Canopy],ids:Set[String],typename:String):Seq[CanopyTagData]={
    val listKnnResult = canopys.map(k=>{
      k.computeTags() //计算下标签
      val n = k.points.size*10
      CanopyTagData(k.getTags, Knn.searchIdsByVector(k.getCenter.x,ClassConstant.mapGraph(typename),n,ids),k.getWeight)
    })
    listKnnResult
  }

  def resultToJson(uid:Long,typeString:String,classResult:Seq[CanopyTagData]):String={
//    val jsonClassResult = classResult.map(tp => {
//      tp._2.map(item => JsonClusterItem(item.tags, item.idWeights.map(_._1), item.weight))
//      typeResult
//    })
//    val date = new Date()
    val result="uid 1, tags,爱情,偶像 , List"
//    Jedis.putJedis("mix.lsy","-1",defaulet);
    ""
  }
  def writeToRedis(labelData:java.util.LinkedHashMap[String,String]):Unit={
//    Jedis.putJedis("mix.lsy","-1",defaulet);
  }
  //遍历所有的簇  初始化影片簇的标签  设置该簇的权重和影片的数量

  //设置簇的ids ，设置簇的标签 ，设置簇的权重   设置簇的最新时间
  //以交集方式处理标签 设置为4个
  //召回影片  聚类代表的向量，类型 和影片个数
//  val onesNeighbors = offlineNeighbors.getNeighbors(videoVector, videoType, clusterNum)



  // 截取类型 推荐几部影片  根据权重排序
  // 聚类结果封装的对象
  // 在list中添加结果聚类

  //时间衰减器  list 中的聚类的Item *０.99

  //对重复标签添加地区标签
//  typeResult.groupBy(_.getTags.toSet).values.filter(_.size>1).foreach(clusters=>clusters.foreach(_.handleConflictTag()))
  //todo 在这里添加排序
  //返回影片类型->List[ClusterItem]元组  指定个数

  //存在返回相应的聚类，不存在返回默认的推荐结果

//  SVideoType.values.keySet.map(vType=>{
//    vType->result.getOrElse(vType,Constant.defaultClass(vType))
//  }).toMap
















  //设置簇的ids ，设置簇的标签 ，设置簇的权重   设置簇的最新时间
  //以交集方式处理标签 设置为4个
  //召回影片  聚类代表的向量，类型 和影片个数
  //  val onesNeighbors = offlineNeighbors.getNeighbors(videoVector, videoType, clusterNum)



  // 截取类型 推荐几部影片  根据权重排序
  // 聚类结果封装的对象
  // 在list中添加结果聚类

  //时间衰减器  list 中的聚类的Item *０.99

  //对重复标签添加地区标签
  //  typeResult.groupBy(_.getTags.toSet).values.filter(_.size>1).foreach(clusters=>clusters.foreach(_.handleConflictTag()))
  //todo 在这里添加排序
  //返回影片类型->List[ClusterItem]元组  指定个数
  //存在返回相应的聚类，不存在返回默认的推荐结果
  //  SVideoType.values.keySet.map(vType=>{
  //    vType->result.getOrElse(vType,Constant.defaultClass(vType))
  //  }).toMap
  //写成json 推荐结果



}
