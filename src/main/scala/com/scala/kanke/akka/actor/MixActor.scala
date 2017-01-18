package com.scala.kanke.akka.actor

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.java.kanke.utils.kafka.KafkaConsumer
import com.scala.kanke.akka.mixrec
import com.scala.kanke.common.{ConfigMix, ConfigClass, MixConstant}
import com.scala.kanke.dao.DaoImpl
import com.scala.kanke.server.mix.MixMain._
import com.scala.kanke.server.mix.MixServerImpl
import com.scala.kanke.utils.Jedis
import kafka.consumer.ConsumerIterator
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2017/1/4.
  */
class MixActor extends Actor {

  val logger = Logger.getLogger(getClass)
  val mixgraph = MixConstant.mapGraph
  val dao = new DaoImpl
  val service = new MixServerImpl()
  val VideoMap =redisSave

  override def receive: Receive = {
    case mixrec(userid,vedioid) =>recByKafka(userid)
    case _ =>
  }



  def recByKafka(userid:String):Unit={
      var resultMap = scala.collection.mutable.Map[String,Double]()
      for(k<- ConfigClass.classtypename){
        val userHistory = dao.queryByUserIdHistory(userid,k)  //查找历史
        resultMap= resultMap++service.startAllServerMix(k,userHistory)
      }
      //构建推荐排序
      if(resultMap.nonEmpty){
        val jedisReslut = resultMap.toList.sortWith(_._2>_._2).take(200).map(x=>x._1).reduceLeft((x, y)=> x+";"+y)
        logger.info(ConfigMix.mixkey+"用户："+userid+"----推荐结果:"+jedisReslut);
        Jedis.putJedis(ConfigMix.mixkey,userid,jedisReslut)
      }
  }

}
