package com.scala.kanke.server.tags

import com.google.gson.Gson
import com.scala.kanke.common.{ConfigMix, TagConfigClass, Constant}
import com.scala.kanke.dao.DaoImpl
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2016/12/21.
  */
object TagsMain {
  val log = Logger.getLogger(getClass)
  Constant.allGraph
  val dao = new DaoImpl
  val service = new TagClassServerImpl()
  //存在返回相应的聚类，不存在返回默认的推荐结果
  val default = service.getOrDefault
  def main(args: Array[String]) {

    while (true) {
      val users = dao.queryAllUserId()
      for (u <- users) {
          var resultMap = scala.collection.mutable.Map[String, Double]()
          val userHistory = dao.queryHistoryByUserId(u.toString) //查找历史
          val json = service.startServerTags(userHistory)
          log.info(TagConfigClass.prefix+"推荐的结果："+u.toString+"    " + json)
          Jedis.putJedis(TagConfigClass.prefix, u.toString, json);
      }
      Thread.sleep(ConfigMix.time)
    }
  }
}
