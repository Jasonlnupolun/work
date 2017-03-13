package com.scala.kanke.server.mix

import com.google.gson.Gson
import com.java.kanke.utils.kafka.KafkaConsumer
import com.scala.kanke.bean.KafkaBean
import com.scala.kanke.common.{ConfigMix, ConfigClass}
import com.scala.kanke.dao.EsMixDao
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2017/3/13.
  */
class MixRecRunable(esMixDao:EsMixDao,service:MixServerImpl) extends  Runnable{

  val logger = Logger.getLogger(getClass)
  val gson = new Gson()
  override def run(): Unit = {

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
    var resultMap = scala.collection.immutable.Map[String, Double]()
    for (k <- ConfigClass.classtypename) {
      val userHistory = esMixDao.queryByUserIdHistory(u.toString,k)  //查找历史
      if(userHistory!=null&&userHistory.size>0){
        resultMap= resultMap ++ service.startKafkaServerMix(k,userHistory)
      }
    }
    //构建推荐排序
    if (resultMap.nonEmpty) {
      val jedisReslut = resultMap.toList.sortWith(_._2 > _._2).take(200).map(x => x._1).reduceLeft((x, y) => x + ";" + y)
      logger.info(ConfigMix.mixkey + "用户：" + u.toString + "----推荐结果:" + jedisReslut);
      Jedis.putJedis(ConfigMix.mixkey, u.toString, jedisReslut)
    }
  }



}
