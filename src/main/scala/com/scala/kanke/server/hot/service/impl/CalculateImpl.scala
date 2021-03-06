package com.scala.kanke.server.hot.service.impl

import com.java.kanke.utils.bean.Video
import com.scala.kanke.server.hot.service.Calculate

/**
  * Created by Administrator on 2016/11/8.
  */
class CalculateImpl extends Calculate{

  override def statistics(listvideo:Seq[Video]): Map[String, Seq[String]] = {
      val listmap = listvideo.take(100).map(x=>x.getId)
      val rateIncrease = increaseRate1(listvideo)
      val map2result = rateIncrease.diff(listmap.take(6))
      val map1= Map("hot"->listmap)
      val map2 = Map("uprate"->map2result)
      val result =map1++map2
      result
  }

  /**
    * 统计周播放量
    */
  override def pop(): Unit = {

  }

  /**
    *
    * 上升最快的
    * 周/月 + 日/周
    */
  override def increaseRate1(listvideo:Seq[Video]): Seq[String] = {
    val map1= listvideo.map(x=>(x.getId,
      (x.getWeekcount/(x.getMonthcount+x.getWeekcount+1)+x.getPlaycount+x.getWeekcount).toDouble))
    val result = map1.sortWith(_._2>_._2).map(x=>x._1)
    result
  }
}
