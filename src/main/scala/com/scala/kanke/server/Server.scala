package com.scala.kanke.server

import java.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.common._
import com.scala.kanke.dao.DaoImpl
import com.scala.kanke.server.mix.MixServerImpl
import com.scala.kanke.server.tags.TagClassServerImpl
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2017/1/19.
  */
object Server {
  val logger = Logger.getLogger(getClass)
  val  gson = new  Gson()
  val all = TagsConstant.allGraph
  val datamap = TagsConstant.dataMap
  val tagdao = TagsConstant.dao
  val tagservice = new TagClassServerImpl()
  //存在返回相应的聚类，不存在返回默认的推荐结果
  val tagdefault = tagservice.getOrDefault

  val classMap = MixConstant.mapGraph
  val mixdao = new DaoImpl
  val mixservice = new MixServerImpl()
  val VideoMap =redisSave
  def main(args: Array[String]) {
    while (true) {
      val users = tagdao.queryAllUserId()
      for (u <- users) {

        val mixresult = runAllMix(u)
        val tagsresult = runAllTags(u)
        val mainMixResult = mixresult._2.split(";").take(6)
        val tagsMap = gson.fromJson(tagsresult._2,new TypeToken[util.LinkedHashMap[String, String]]() {
        }.getType())
      }
      Thread.sleep(ConfigMix.time)
    }
  }
  def runAllTags(u:String):Tuple2[String,String]={
        val userHistory = tagdao.queryHistoryByUserId(u.toString) //查找历史
        val json = tagservice.startServerTags(userHistory)
        logger.info(TagConfigClass.prefix+"推荐的结果："+u.toString+"    " + json)
        (u, json);

  }

  def runAllMix(u:String):Tuple2[String,String]={
    // 全量分析
        var resultMap = scala.collection.mutable.Map[String, Double]()
        for (k <- ConfigClass.classtypename) {
          val userHistory = mixdao.queryByUserIdHistory(u, k) //查找历史
          resultMap = resultMap ++ mixservice.startAllServerMix(k, userHistory)
        }
        //构建推荐排序
        val res = if (resultMap.nonEmpty) {
          val jedisReslut = resultMap.toList.sortWith(_._2 > _._2).take(200).map(x => x._1).reduceLeft((x, y) => x + ";" + y)
          logger.info(ConfigMix.mixkey + "用户：" + u.toString + "----推荐结果:" + jedisReslut);
          (u, jedisReslut)
        }else ("","")
        res
  }



  /**
    * 保存 年 ，播放量 ，评分 保存到redis中
    */
  def redisSave():Unit={
    import collection.JavaConversions._
    val listvideo:java.util.List[Video] = DBCommon.queryForBean(ConfigMix.initsavereids,null,classOf[Video])
    listvideo.map(x=>{
      if(x.getPlaycount==null){
        x.setPlaycount(0)
      }
      if(x.getKankeid.contains("_"))
        Jedis.putJedis(x.getVideotype,x.getKankeid,x.getYear+";"+x.getPlaycount+";"+x.getScore)
    })
  }

}
