package com.scala.kanke.server.mix

import com.google.gson.Gson
import com.java.kanke.utils.kafka.KafkaConsumer
import com.scala.kanke.bean.KafkaBean
import com.scala.kanke.common.{MixConstant, ConfigClass, ConfigMix, Constant}
import com.scala.kanke.dao.{EsMixDaoImpl, DaoImpl}
import com.scala.kanke.server.mix.MixMain._
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2017/1/18.
  */
class MixRec {

  val logger = Logger.getLogger(getClass)
  val gson = new Gson()
  MixConstant.mapGraph
  val dao = new DaoImpl
  val service = new MixServerImpl()
  val VideoMap =redisSave
  val esMixDao = new EsMixDaoImpl

  def runAllMix(): Unit = {
    // 全量分析
    while (true) {
      val users = dao.queryAllUserId()
      for (u <- users) {
        getRecResult(u)
      }
      Thread.sleep(ConfigMix.time)
    }
  }


  def runFromKafka(): Unit ={
    val it = new KafkaConsumer().consume();
    while (it.hasNext()) {
      try {
        val tb = gson.fromJson(it.next.message, classOf[KafkaBean]);
        esMixDao.saveUserHistoryById(tb)
        val u =  tb.userId
        getRecResult(u)
      } catch {
        case e: Exception => e.printStackTrace(); logger.info("错误的json字符========")
      }
    }
  }


  // 根据用户id 计算推荐的结果放回到redis中
  def getRecResult(u:String):Unit={
    var resultMap = scala.collection.mutable.Map[String, Double]()
    for (k <- ConfigClass.classtypename) {
      val userHistory = dao.queryByUserIdHistory(u.toString, k) //查找历史
      resultMap = resultMap ++ service.startAllServerMix(k, userHistory)
    }
    //构建推荐排序
    if (resultMap.nonEmpty) {
      val jedisReslut = resultMap.toList.sortWith(_._2 > _._2).take(200).map(x => x._1).reduceLeft((x, y) => x + ";" + y)
      logger.info(ConfigMix.mixkey + "用户：" + u.toString + "----推荐结果:" + jedisReslut);
      Jedis.putJedis(ConfigMix.mixkey, u.toString, jedisReslut)
    }
  }

}
