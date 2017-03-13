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

  // 查询历史构建特征向量
  def userVideoToFeatureBean(typename:String,bean:List[Video]):  Seq[FeatureBean] ={
    //根据历史记录，查全信息
    bean.map(x=>Vectoriza.transferFeatureBean(x,Constant.coordinate))
  }

}
