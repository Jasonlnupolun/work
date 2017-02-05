package com.scala.kanke.common

import com.java.kanke.utils.PropertyUtil
import com.scala.kanke.arg.knn.{Knn, Vectoriza}
import com.scala.kanke.dao.{TagsDaoImpl, MixDaoImpl, Dao, DaoImpl}

/**
  * Created by Administrator on 2016/11/16.
  */
object Constant {
  val dao = DaoFactory("class")
  val classtypename = PropertyUtil.get("classtypename").split(";");
  val tags = dao.querytags("")
  val regions = dao.queryOrgion("")
  val coordinate = (tags++regions) diff ConfigClass.labelremove
  // tags 标签进行 混合推荐
}

object DaoFactory{
    def apply(kind: String) = kind match {
    case "class" => new DaoImpl
    case "mix" => new MixDaoImpl
    case "tags" => new TagsDaoImpl
   }
}




object MixConstant{

  val list = Constant.classtypename

  val dao = DaoFactory("mix")
  val tags = dao.querytags("")
  val regions = dao.queryOrgion("")
  val coordinate = (tags++regions) diff ConfigClass.labelremove
  import scala.collection.JavaConversions._
  val district = dao.queryDistrict.map(x=>(x._1,x._2.split(",")))
  val mapGraph = list.map(x=>(x,
    Knn.knnGraph(1000,new Vectoriza(dao.findByType(x),coordinate).vectorizer())
    )).toMap


}

object ClassConstant{
  val dao = DaoFactory("class")
  val mapGraph = Constant.classtypename.map(x=>(x,
    Knn.knnGraph(1000,new Vectoriza(dao.findByType(x),Constant.coordinate).vectorizer())
    )).toMap
}

object TagsConstant{
  val dao = DaoFactory("tags")
  val dataMap = dao.queryMapDate
  val allGraph =
    Knn.knnGraph(1000,new Vectoriza(dao.findAll,Constant.coordinate).vectorizer())
}




