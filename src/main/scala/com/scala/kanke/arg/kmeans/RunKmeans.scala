package com.scala.kanke.arg.kmeans

import com.scala.kanke.arg.knn.{Vectoriza, Knn}
import com.scala.kanke.dao.DaoImpl

/**
  * Created by Administrator on 2017/2/7.
  */
object RunKmeans {


  //读去数据，配置初始化数据
  val dao = new DaoImpl
  val tags = dao.querytags("")
  val regions = dao.queryOrgion("")

  def main(args: Array[String]) {
    val features= new Vectoriza(dao.findAll,tags++regions).vectorizer()
  }
}
