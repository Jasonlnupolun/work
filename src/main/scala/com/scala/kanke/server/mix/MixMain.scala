package com.scala.kanke.server.mix

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.kafka.KafkaConsumer
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.common.{MixConstant, ConfigClass, ConfigMix, Constant}
import com.scala.kanke.dao.DaoImpl
import com.scala.kanke.utils.Jedis
import kafka.consumer.ConsumerIterator
import org.apache.log4j.Logger
object MixMain{
  val logger = Logger.getLogger(getClass)
  MixConstant.mapGraph
  val dao = new DaoImpl
  val service = new MixServerImpl()
  val VideoMap =redisSave

  def main(args: Array[String]) {
    defaultVideo
    new MixRec().runAllMix()
//    recByKafka
  }

  def defaultVideo(): Unit ={
    val defaulet = dao.queryDefaultVideo().map(x=>x.getKankeid).reduceLeft((x, y)=> x+";"+y);
    Jedis.putJedis(ConfigMix.mixkey,"-1",defaulet);
  }

  /**
    * 保存 年 ，播放量 ，评分 保存到redis中
    */
  def redisSave():Unit={
    import collection.JavaConversions._
    val listvideo:java.util.List[Video] = DBCommon.queryForBean(ConfigMix.initsavereids,null,classOf[Video])
    listvideo.map(x=>{
      if(x.getPlaycount==null){
        x.setPlaycount(0)
      }
      if(x.getKankeid.contains("_"))
      Jedis.putJedis(x.getVideotype,x.getKankeid,x.getYear+";"+x.getPlaycount+";"+x.getScore)
    })
  }

  // 根据kafka进行增量分析

  def recByKafka():Unit={
    val it: ConsumerIterator[String, String] = new KafkaConsumer().consume
    while (it.hasNext()){
        var resultMap = scala.collection.mutable.Map[String,Double]()
        for(k<- ConfigClass.classtypename){
          val userHistory = dao.queryByUserIdHistory(it.next().message().toString,k)  //查找历史
          resultMap= resultMap++service.startAllServerMix(k,userHistory)
        }
        //构建推荐排序
        if(resultMap.nonEmpty){
          val jedisReslut = resultMap.toList.sortWith(_._2>_._2).take(200).map(x=>x._1).reduceLeft((x, y)=> x+";"+y)
          logger.info(ConfigMix.mixkey+"用户："+it.next().message().toString+"----推荐结果:"+jedisReslut);
          Jedis.putJedis(ConfigMix.mixkey,it.next().message().toString,jedisReslut)
      }
    }

  }

}