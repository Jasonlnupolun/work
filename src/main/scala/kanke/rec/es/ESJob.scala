package kanke.rec.es

import com.google.gson.Gson

/**
  * Created by gavin on 16-4-12.
  * northland89@163.com 
  */

abstract class ESJob[T](classType:Class[T]) {
  def getSql:String
  def jsonToBean[T](string: String,classType:Class[T]):T={
    val gson=new Gson()
    gson.fromJson(string,classType)
  }
}
