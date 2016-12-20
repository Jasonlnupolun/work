package com.scala.kanke.common

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.arg.knn.{Knn, Vectoriza}
import com.scala.kanke.dao.DaoImpl

/**
  * Created by Administrator on 2016/11/16.
  */
object Constant {

  val list = List[String]("tv","arts","film","anime")
  val formatter = new java.text.DecimalFormat("#.000")
  val dao = new DaoImpl
  val tags = dao.querytags("")
  val regions = dao.queryOrgion("")
  val coordinate = tags++regions
  val mapGraph = list.map(x=>(x,
    Knn.knnGraph(20,new Vectoriza(dao.findByType(x),coordinate).vectorizer())
    )).toMap


}
