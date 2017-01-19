package com.scala.kanke.server.tags

import com.scala.kanke.common.{TagsConstant, ConfigMix, TagConfigClass}
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2016/12/21.
  */
object TagsMain {
  val log = Logger.getLogger(getClass)
  TagsConstant.allGraph
  val datamap = TagsConstant.dataMap
  val dao = TagsConstant.dao
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
