package com.scala.kanke.dao

import java.util

import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.common.{TagConfigClass, TagsConstant}

/**
  * Created by Administrator on 2017/1/19.
  */
class TagsDaoImpl extends  DaoImpl{
  override def queryMapDate: util.Map[String, util.Map[String,Object]] = {
    DBCommon.queryMapList(TagConfigClass.datamap,null)
  }
}
