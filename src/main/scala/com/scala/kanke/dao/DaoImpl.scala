package com.scala.kanke.dao

import java.util

import com.java.kanke.utils.bean.{UserHistory, Video}
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.arg.knn.{Vectoriza, FeatureBean}
import com.scala.kanke.common.{TagConfigClass, ConfigMix, Constant}
import org.apache.log4j.Logger
/**
  * Created by Administrator on 2016/11/11.
  */

trait Dao {

  def queryKankeFeaturById(userHistory:UserHistory): FeatureBean
  def querytags(sql:String):Array[String]
  def queryOrgion(sql:String):Array[String]
  def findByType(userid:String):List[Video]
  def queryByUserIdHistory(sql:String,typename:String):List[UserHistory]
  def queryAllHistory(userid: String):Unit
  def queryAllUserId():List[String]
  def update(sql:String ,array:Array[Object] )
  def queryDefaultVideo():List[Video]
  def queryByKankeid(userHistory:UserHistory):Video
  def queryAllYears():Map[String,String]
  def findAll():List[Video]
  def queryHistoryByUserId(userid: String): List[UserHistory]
  def queryDistrict:java.util.Map[String,java.util.Map[String,String]]
  def queryMapDate:util.Map[String, util.Map[String,Object]]
}

abstract class ElasSql extends  Dao{


  override def findByType(userid: String): List[Video] = ???

  override def update(sql: String, array: Array[Object]): Unit = ???

  override def queryAllYears(): Map[String, String] = ???

  override def querytags(sql: String): Array[String] = ???

  override def queryDefaultVideo(): List[Video] = ???

  override def findAll(): List[Video] = ???

  override def queryHistoryByUserId(userid: String): List[UserHistory] = ???

  override def queryKankeFeaturById(userHistory: UserHistory): FeatureBean = ???

  override def queryByUserIdHistory(sql: String, typename: String): List[UserHistory] = ???

  override def queryByKankeid(userHistory: UserHistory): Video = ???

  override def queryOrgion(sql: String): Array[String] = ???

  override def queryAllHistory(userid: String): Unit = ???

  override def queryAllUserId(): List[String] = ???

  override def queryDistrict: java.util.Map[String,java.util.Map[String,String]] = ???
}

class DaoImpl extends  Dao{
  val log = Logger.getLogger(getClass)

  //根据当前时间段
  override def findByType(tyname:String): List[Video] ={
    val sql = "SELECT id ,kanke_id as kankeid, video_type as videotype ,year ,tag as tags, region" +
      "  from t_vod_combine tvc  where tvc.video_type = ?  and kanke_id regexp '" +
      tyname+"'"+" and is_matched = 1 "
    val array = new Array[Object](1)
    array(0)=tyname
    import collection.JavaConversions._
    val listvideo = DBCommon.queryForBean(sql,array,classOf[Video]).toList;
    log.info("查询数据库的结果集是：" +listvideo.size)
    listvideo
  }


  override def findAll(): List[Video] ={
    import collection.JavaConversions._
    val listvideo = DBCommon.queryForBean(TagConfigClass.all,null,classOf[Video]).toList;
    log.info("查询数据库的结果集是：" +listvideo.size)
    listvideo
  }

  // 根据用户查询该用户的历史数据
  override def queryByUserIdHistory(userid: String,typename:String): List[UserHistory] = {
    val array = new Array[Object](2)
    array(0)=userid
    array(1)=typename
    import collection.JavaConversions._
    val listvideo = DBCommon.queryForBean(ConfigMix.userhistorysql,array,classOf[UserHistory]).toList
    listvideo
  }

  // 根据用户查询该用户的历史数据
  override def queryHistoryByUserId(userid: String): List[UserHistory] = {
    val array = new Array[Object](1)
    array(0)=userid
    import collection.JavaConversions._
    val listvideo = DBCommon.queryForBean(TagConfigClass.useridhistory,array,classOf[UserHistory]).toList
    listvideo
  }

  //查找所用用户的id
  override  def queryAllUserId():List[String]={
    import collection.JavaConversions._
    val kankeids  = DBCommon.queryColumnListHandler(ConfigMix.useridsql,"userid",null)
    kankeids.toList
  }

  override def queryAllHistory(userid: String): Unit = {
    val sql = "SELECT id ,video_type as videotype ,year ,tag as tags, region " +
      " from t_vod_combine tvc  where tvc.video_type = ?"
    val array = new Array[Object](1)
//    array(0)=tyname
    import collection.JavaConversions._
    val listvideo = DBCommon.queryForBean(sql,array,classOf[Video]).toList;
    log.info("查询数据库的结果集是：" +listvideo.size)
//    listvideo
    val list = List

  }

  //根据看客的id 查询特征
  override def queryKankeFeaturById(userHistory:UserHistory): FeatureBean = {
    val array = new Array[Object](1)
    array(0)=userHistory.getKankeid
    import collection.JavaConversions._
    val video = DBCommon.queryBean(ConfigMix.featurebyidsql,array,classOf[Video]);
    if(video !=null ){
      Vectoriza.transferFeatureBean(video,Constant.coordinate)
    }else{
      null
    }

  }


  //查询标签集合
  override def querytags(sql: String): Array[String] = {
    var sql = "select region,tag from t_vod_combine where tag <> ''"
    import collection.JavaConversions._
    val tags= DBCommon.queryColumnListHandler(sql,"tag",null).asInstanceOf[java.util.List[String]].map(x=>{
        x.split(";")
    })
    val tagsSet = tags.flatMap(x=>x).toSet
    (tagsSet).toArray
  }

  //查询地区集合
  override def queryOrgion(params:String):Array[String]={
    import collection.JavaConversions._
    var sql = "select region,tag from t_vod_combine where region <> ''"
    val region = DBCommon.queryColumnListHandler(sql,"region",null).asInstanceOf[java.util.List[String]].map(x => x.split(";"))
    val regionSet = region.flatMap(x=>x).toSet
    regionSet.toArray
  }

  // 更新iti
  override def update(table: String, array: Array[Object]): Unit = {
    val sql = " insert into iti"+table+" (idname,idlist) values(?,?)"
    DBCommon.saveBean(sql,array)
  }

  override def queryDefaultVideo(): List[Video] = {
    import collection.JavaConversions._
    val listvideo = DBCommon.queryForBean(ConfigMix.defaultSql,null,classOf[Video]).toList;
    listvideo
  }

  override def queryByKankeid(userHistory:UserHistory): Video = {
    val sql = "SELECT id ,kanke_id as kankeid ,year ,tag as tags, region " +
      "from t_vod_combine tvc   order by `year` desc , play_count desc  limit 20 "
    import collection.JavaConversions._
    val  videoBean = DBCommon.queryBean(sql,null,classOf[Video]).asInstanceOf[Video]
    videoBean
  }

  override def queryAllYears(): Map[String,String] = {
    val sql = "select  kanke_id, case    " +
      "  when   (`year`>=2008) then   '5' " +
      "  when   (`year`>=1990 and `year`<2008)    then   '4' " +
      "  when   (`year`>=1980 and `year`<1990)    then    '3' " +
      "  when   (`year`>=1970 and `year`<1980)    then    '2' " +
      "  when   (`year`>=1950 and `year`<1970)    then    '1' " +
      "  else     '0' " +
      "  end  as num  from   t_vod_combine "
    import collection.JavaConversions._
    val  videoBean = DBCommon.queryForMap(sql,null)
    videoBean.toMap
  }

  override def queryDistrict: java.util.Map[String, java.util.Map[String,String]] = ???

  override def queryMapDate: java.util.Map[String, java.util.Map[String,Object]] = ???
}


class MixDaoImpl extends  DaoImpl{

      override  def queryDistrict={
        val sql = ConfigMix.district
        DBCommon.queryMapList(sql,null).asInstanceOf[java.util.Map[String,java.util.Map[String,String]]]
      }

}
