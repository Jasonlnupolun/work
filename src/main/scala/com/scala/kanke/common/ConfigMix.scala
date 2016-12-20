package com.scala.kanke.common

import com.java.kanke.utils.PropertyUtil

/**
  * Created by Administrator on 2016/11/23.
  */
object ConfigMix {

  val time = PropertyUtil.get("updatetime").toInt*60000L;
  val useridsql =  PropertyUtil.get("userids")
  val userhistorysql = PropertyUtil.get("userhistory")
  val defaultSql =PropertyUtil.get("defaultSql")
  val featurebyidsql = PropertyUtil.get("featurebyid")
  val initsavereids = PropertyUtil.get("initsavereids")
  val mixkey = PropertyUtil.get("mixkey")
}
