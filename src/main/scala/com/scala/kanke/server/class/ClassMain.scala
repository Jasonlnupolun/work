package com.scala.kanke.server

import com.google.gson.Gson
import com.scala.kanke.common.{ClassConstant, ConfigClass}
import com.scala.kanke.dao.DaoImpl
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger
/**
  * Created by Administrator on 2016/11/21.
  */
object ClassMain {
  val log = Logger.getLogger(getClass)
  ClassConstant.mapGraph
  val dao = new DaoImpl
  val service = new ClassServerImpl()
  //存在返回相应的聚类，不存在返回默认的推荐结果
  val default= ConfigClass.classtypename.map(x=>(x,service.getOrDefault(x))).toMap
  defaultdata
  def main(args: Array[String]) {
    while(true){
      val users = dao.queryAllUserId()
      for(u <- users){
        var resultMap = scala.collection.mutable.Map[String,Double]()
        for(k <- ConfigClass.classtypename){
          val defaultdata = default(k)
          val userHistory = dao.queryByUserIdHistory(u.toString,k)  //查找历史
          val json = service.startServerClass(userHistory,defaultdata,k)
          log.info(ConfigClass.prefix+k+"   推荐的结果："+u.toString+"   "+json)
          Jedis.putJedis(ConfigClass.prefix+k,u.toString,json);
          }
      }
      Thread.sleep(1000*60*5)
    }
  }


  def defaultdata(): Unit ={
    for((k,v)<- default){
      var labelMap = new java.util.LinkedHashMap[String,String]() // 使簇内影片不重复
      labelMap.put("热门",default(k).take(50).map(x=>x.getKankeid).mkString(";"))
      labelMap.put("其他",default(k).slice(50,100).map(x=>x.getKankeid).mkString(";"))
      val gson = new Gson
      val jsonStr = gson.toJson(labelMap);
      Jedis.putJedis(ConfigClass.prefix+k,"-1",jsonStr);
    }

  }

}
