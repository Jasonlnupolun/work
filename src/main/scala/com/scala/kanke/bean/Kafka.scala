package com.scala.kanke.bean

/**
  * Created by Administrator on 2017/2/20.
  */
class Kafka {

}

case class KafkaBean(userId:String,videoId:String,videoType:String,opTime:String,recommend:String,englishName:String)

case class EsHistoryTable(userid:String,kankeid:String,year:Int,score:Double,tags:String,region:String,videotype:String,playcount:Int,sources:String,addtime: String,englishName:String,flag:String="")

