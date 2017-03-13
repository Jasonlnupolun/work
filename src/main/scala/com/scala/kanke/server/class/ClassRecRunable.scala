package com.scala.kanke.server

import com.java.kanke.utils.bean.Video
import com.scala.kanke.common.ConfigClass
import com.scala.kanke.dao.Dao
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger


/**
  * Created by Administrator on 2017/3/13.
  */
class ClassRecRunable(dao:Dao,service:ClassServer,u:String,default:Map[String,List[Video]]) extends  Runnable{
  val log = Logger.getLogger(getClass)
  override def run(): Unit = {
    getClassRecResult(u)
  }
  def getClassRecResult(u:String)={
    var resultMap = scala.collection.mutable.Map[String,Double]()
    for(k <- ConfigClass.classtypename){
      val defaultdata = default(k)
      val userHistory = dao.queryByUserIdHistory(u.toString,k)  //查找历史
      val json = service.startServerClass(userHistory,defaultdata,k)
      log.info(ConfigClass.prefix+k+"   推荐的结果："+u.toString+"   "+json)
      Jedis.putJedis(ConfigClass.prefix+k,u.toString,json);
    }
  }

}
