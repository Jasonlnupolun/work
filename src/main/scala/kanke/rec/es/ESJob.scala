package kanke.rec.es

import com.google.gson.Gson

/**
  */

abstract class ESJob[T](classType:Class[T]) {
  def getSql:String
  def jsonToBean[T](string: String,classType:Class[T]):T={
    val gson=new Gson()
    gson.fromJson(string,classType)
  }
}
