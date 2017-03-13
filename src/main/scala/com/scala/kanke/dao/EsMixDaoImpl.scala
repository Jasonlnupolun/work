package com.scala.kanke.dao

import java.text.SimpleDateFormat
import java.util.Date
import com.java.kanke.utils.bean.{Video}
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.bean.{EsHistoryTable, KafkaBean}
import com.scala.kanke.common._
import kanke.rec.es.{QueryGroupMixUserid, QueryMixUserid, QueryUserid, ESClient}
/**
  * Created by Administrator on 2017/2/17.
  */
trait  EsMixDao {
  def queryByUserIdHistory(sql:String,typename:String):List[Video]
  def queryByGroupHistory(sql:String):List[Video]
  def saveUserHistoryById(kafkaBean:KafkaBean)
}

class EsMixDaoImpl extends  EsMixDao{
  val eSClient = ESClient( elasticip,elasticport,clustername)
  override def queryByUserIdHistory(userid: String, typename: String): List[Video] = {
    val videos  = eSClient.query(QueryMixUserid(userid,typename),classOf[Video])
    videos.toList
  }

  // 查询数据
  override def queryByGroupHistory(userid: String): List[Video] = {
    val videos  = eSClient.query(QueryGroupMixUserid(userid),classOf[Video])
    videos.toList
  }

  // 根据数据库中的数据进行组装放入redis中
  override def saveUserHistoryById(kafkaBean:KafkaBean): Unit = {
    val videoType = getCharToType(kafkaBean.videoType)
    if(videoType!="error"){
//      val sql = useridhistory.format("crawler_"+videoType)
//      val params = new Array[Object](1)
//      params(0)=kafkaBean.videoId
//      val v = DBCommon.queryBean(sql,params,classOf[Video])
//      var format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//      var year = ""
//      if(v.getYear.contains("|")&&v.getYear.contains("年代")){
//        year = v.getYear.split("\\|")(0)
//      }
//      if(!verify(year,"Int")){
//        year = "1970"
//      }
//      val t = EsHistoryTable(kafkaBean.userId,v.getId,year.toInt,v.getScore.toDouble,v.getTags,v.getRegion,videoType,v.getPlaycount.toInt,kafkaBean.recommend,format.format(new Date()), kafkaBean.englishName)
//      eSClient.sava("jsrec","kafka",t)
    }
  }
}
