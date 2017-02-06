package com.scala.kanke.common

import com.java.kanke.utils.PropertyUtil
import com.scala.kanke.utils.ConfigUtils

/**
  * Created by Administrator on 2016/11/23.
  */
object ConfigMix {

  val tool = ConfigUtils("mix.properties")

  val time = PropertyUtil.get("updatetime").toInt*60000L;

  val mixkey = PropertyUtil.get("mixkey")

  val labelremove =PropertyUtil.get("labelremove").split(";")

//sql
  val useridsql =  tool.getString("userids")
  val userhistorysql = tool.getString("userhistory")
  val defaultSql =tool.getString("defaultSql")
  val featurebyidsql = tool.getString("featurebyid")
  val initsavereids = tool.getString("initsavereids")
  val district = tool.getString("districtsql")


}
