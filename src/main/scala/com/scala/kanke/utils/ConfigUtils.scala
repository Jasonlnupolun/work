package com.scala.kanke.utils

import com.java.kanke.utils.PropertyUtil
import com.typesafe.config.{ConfigFactory, Config}

/**
  * Created by Administrator on 2016/12/9.
  */
object ConfigUtils {

  val config: Config = ConfigFactory.load

}
