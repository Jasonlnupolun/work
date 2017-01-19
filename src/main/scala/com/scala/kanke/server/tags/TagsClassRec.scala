package com.scala.kanke.server.tags

import com.scala.kanke.common.{TagConfigClass, TagsConstant}
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2017/1/19.
  */
class TagsClassRec {
  val logger = Logger.getLogger(getClass)

  val all = TagsConstant.allGraph
  val datamap = TagsConstant.dataMap
  val tagdao = TagsConstant.dao
  val tagservice = new TagClassServerImpl()

  def runAllTags(u:String)={
    val userHistory = tagdao.queryHistoryByUserId(u.toString) //查找历史
    val json = tagservice.startServerTags(userHistory)
    logger.info(TagConfigClass.prefix+"推荐的结果："+u.toString+"    " + json)
    (u, json);

  }
}
