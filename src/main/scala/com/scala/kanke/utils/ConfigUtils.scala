package com.scala.kanke.utils

import java.io.File

import com.java.kanke.utils.PropertyUtil
import com.typesafe.config.{ConfigFactory, Config}

/**
  * Created by Administrator on 2016/12/9.
  */
object ConfigUtils {

  val path = System.getProperty("user.dir")+File.separator+"conf"+File.separator;
  def apply(tpath:String)=ConfigFactory.parseFile(new File(path+tpath))

  def main(args: Array[String]) {
   println(ConfigUtils("class.properties").getString("cartoon"))
  }


}
