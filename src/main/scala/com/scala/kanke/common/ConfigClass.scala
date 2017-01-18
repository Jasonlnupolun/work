package com.scala.kanke.common

import com.java.kanke.utils.PropertyUtil

/**
  * Created by Administrator on 2016/11/23.
  */
object ConfigClass {

  val classtypename = PropertyUtil.get("classtypename").split(";");
  val defaultclass  = PropertyUtil.get("defaultclass")
  val prefix = PropertyUtil.get("prefix")
  val labelremove = PropertyUtil.get("labelremove").split(";")
  val cartoon = PropertyUtil.get("cartoon")



}
