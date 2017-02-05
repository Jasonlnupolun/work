package com.scala.kanke.service

import com.java.kanke.utils.bean.{Video, UserHistory}
import com.scala.kanke.arg.knn.{FeatureBean, Vectoriza}
import com.scala.kanke.common.Constant
import com.scala.kanke.dao.DaoImpl

/**
  * Created by Administrator on 2016/11/16.
  */
trait UserService

class UserHistoryService extends  UserService{

  val dao = new DaoImpl

  def userHistoryToVideo(bean:UserHistory):  List[Video] ={
    //根据历史记录，查全信息
    val video = new Video
    val listvideo = List(video)
    listvideo
  }

  // 查询历史构建特征向量
  def userHistoryToVideo(bean:List[UserHistory]):  Seq[FeatureBean] ={
    //根据历史记录，查全信息
    bean.map(x=>{
      val video = new  Video()
      video.setUserid(x.getUserid)
      video.setKankeid(x.getKankeid)
      video.setTags(x.getTags)
      video.setVideotype(x.getTypename)
      video.setYear(x.getYear)
      video.setRegion(x.getRegion)
      Vectoriza.transferFeatureBean(video,Constant.coordinate)
    })
  }




  //历史记录总数


//  var typeMap = Map[Long, WeightedVideo]()

//  val clusterResult = cnpy.cluster()

  //该类型分得多少部推荐影片(取200%)  代表占比
//  val typeSize = (Constant.MixTakeSize.toDouble * videoIds.size / totalSize).toInt
  //    遍历簇  遍历代表
//      代表的权重
//      代表的时间
//      代表近邻取值  ｛近邻中的数值要进行去掉重复的近邻｝
//      构建权重
//      //权重衰减,从前到后,按簇衰减簇内影片权重
//      //二次排序（是否留存）
//      //截断
//      typeArray=typeArray.take(typeSize)
//      typeArray = typeArray.sortWith(_.getSecondSortWeight > _.getSecondSortWeight)
      //提取id 构建将要输出数据结构




}
