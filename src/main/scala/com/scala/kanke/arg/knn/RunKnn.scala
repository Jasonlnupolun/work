package com.scala.kanke.arg.knn

import com.scala.kanke.common.Constant
import com.scala.kanke.dao.DaoImpl

/**
  * Created by Administrator on 2016/11/22.
  */
object RunKnn {

  val list = List[String]("tv","arts","film","anime")

  val dao = new DaoImpl
  val tags = dao.querytags("")
  val regions = dao.queryOrgion("")


  def main(args: Array[String]) {
    val mapGraph = list.map(x=>(x,
      Knn.knnDaoGraph(new Vectoriza(dao.findByType(x),tags).vectorizer(),dao,x)
      )).toMap
  }
}
