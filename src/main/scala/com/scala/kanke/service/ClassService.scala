package com.scala.kanke.service

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.common.ConfigClass

/**
  * Created by Administrator on 2016/11/24.
  */
trait ClassService{
  def queryDefaultVideo(typename:String): List[Video]
}
class ClassServiceImpl extends ClassService{


  override def queryDefaultVideo(typename:String): List[Video] = {
    import collection.JavaConversions._
    val params = new Array[Object](1)
    params(0)=typename;
    val listvideo = DBCommon.queryForBean(ConfigClass.defaultclass,params,classOf[Video]).toList;
    listvideo
  }
}