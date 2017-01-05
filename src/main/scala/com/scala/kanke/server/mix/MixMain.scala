package com.scala.kanke.server.mix

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.mysql.DBCommon
import com.scala.kanke.common.{ConfigClass, ConfigMix, Constant}
import com.scala.kanke.dao.DaoImpl
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger
object MixMain{
  val logger = Logger.getLogger(getClass)
  Constant.mapGraph
  val dao = new DaoImpl
  val service = new MixServerImpl()
  val VideoMap =redisSave
  def main(args: Array[String]) {

    // 增量分析
//    val it = new KafkaConsumer().consume()
//    while (it.hasNext) {
//      val bean = JsonToBean.toBean(it.next().message())
//    }
    defaultVideo

    // 全量分析
    while (true){
      val users = dao.queryAllUserId()
      for(u <- users){

        var resultMap = scala.collection.mutable.Map[String,Double]()
        for(k<- ConfigClass.classtypename){
          val userHistory = dao.queryByUserIdHistory(u.toString,k)  //查找历史
          resultMap= resultMap++service.startAllServerMix(k,userHistory)
        }

        //构建推荐排序
        if(resultMap.nonEmpty){
          val jedisReslut = resultMap.toList.sortWith(_._2>_._2).take(200).map(x=>x._1).reduceLeft((x, y)=> x+";"+y)
          logger.info(ConfigMix.mixkey+"用户："+u.toString+"----推荐结果:"+jedisReslut);
          Jedis.putJedis(ConfigMix.mixkey,u.toString,jedisReslut)
        }

      }
      Thread.sleep(ConfigMix.time)
    }
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

}