package com.scala.kanke

import scala.util.{Success, Try}

/**
  * Created by Administrator on 2017/2/20.
  */
package object dao {


  def verify(str: String, dtype: String):Boolean = {

    var c:Try[Any] = null
    if("double".equals(dtype)) {
      c = scala.util.Try(str.toDouble)
    } else if("int".equals(dtype)) {
      c = scala.util.Try(str.toInt)
    }
    val result = c match {
      case Success(_) => true;
      case _ =>  false;
    }
    result
  }

  def getCharToType(itype:String):String= {
    itype match {
      case "M" => "film"
      case "F"=> "film"
      case "T" => "tv"
      case "C" => "anime"
      case "A" => "anime"
      case "D" => "documentary"
      case "E" => "arts"
      case _ => "error"
    }
  }
}
