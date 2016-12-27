package com.scala.kanke.common

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.arg.knn.{Knn, Vectoriza}
import com.scala.kanke.dao.DaoImpl

/**
  * Created by Administrator on 2016/11/16.
  */
object Constant {

  val list = ConfigClass.classtypename
  val formatter = new java.text.DecimalFormat("#.000")
  val dao = new DaoImpl
  val tags = dao.querytags("")
  val regions = dao.queryOrgion("")
  val coordinate = (tags++regions) diff ConfigClass.labelremove
  val mapGraph = list.map(x=>(x,
    Knn.knnGraph(1000,new Vectoriza(dao.findByType(x),coordinate).vectorizer())
    )).toMap

  // tags 标签进行 混合推荐
  val allGraph =
    Knn.knnGraph(1000,new Vectoriza(dao.findAll,coordinate).vectorizer())


}
