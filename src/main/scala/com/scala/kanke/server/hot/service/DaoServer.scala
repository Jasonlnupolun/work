package com.scala.kanke.server.hot.service

import com.java.kanke.utils.bean.Video

/**
  * Created by Administrator on 2016/11/8.
  */
trait DaoServer {

  def queryVideo(sql:String ,obj:Array[Object],clazz: Class[_]):Seq[Video]
}
