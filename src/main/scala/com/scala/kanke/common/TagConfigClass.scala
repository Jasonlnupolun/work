package com.scala.kanke.common

import com.java.kanke.utils.PropertyUtil
import com.scala.kanke.utils.ConfigUtils

/**
  * Created by Administrator on 2016/12/22.
  */
object TagConfigClass {

  val tool = ConfigUtils("tags.properties")

  val all =tool.getString("all")
  val tagDefaultVideo = tool.getString("tagDefaultVideo")
  val useridhistory = tool.getString("useridhistory")
  val prefix = tool.getString("prefixtags")
  val datamap = tool.getString("datamap")


}
