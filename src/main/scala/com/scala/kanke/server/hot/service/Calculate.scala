package com.scala.kanke.server.hot.service

import com.java.kanke.utils.bean.Video

/**
  * Created by Administrator on 2016/11/8.
  */
trait Calculate {

  def statistics(listvideo:Seq[Video]):Map[String, Seq[String]]


  /**
    * 统计周播放量
    */

  def pop()
  /**
    *
    * 上升最快的
    * 周/月 + 日/周
    */
  def increaseRate1(listvideo:Seq[Video]):Seq[String]

}
