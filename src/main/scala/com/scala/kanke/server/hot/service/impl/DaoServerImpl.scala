package com.scala.kanke.server.hot.service.impl

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.server.hot.service.DaoServer

/**
  * Created by Administrator on 2016/11/8.
  */
class DaoServerImpl extends DaoServer{
  override def queryVideo(sql: String, obj: Array[Object], clazz: Class[_]): Seq[Video] = {
    import collection.JavaConverters._
    val videos = DBCommon.queryForBean(sql, obj, clazz).asScala
    videos
  }
}
