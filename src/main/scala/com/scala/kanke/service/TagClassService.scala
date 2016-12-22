package com.scala.kanke.service

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.common.TagConfigClass

/**
  * Created by Administrator on 2016/12/22.
  */
trait TagClassService {

  def queryDefaultVideo(): List[Video]
}

class TagClassServiceImpl extends TagClassService{
  override def queryDefaultVideo(): List[Video] = {
    import collection.JavaConversions._
    val listvideo = DBCommon.queryForBean(TagConfigClass.tagDefaultVideo,null,classOf[Video]).toList;
    listvideo
  }
}



