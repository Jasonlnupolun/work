package com.scala.kanke.server.mix

import com.scala.kanke.common.{MixConstant, ConfigClass, ConfigMix, Constant}
import com.scala.kanke.dao.DaoImpl
import com.scala.kanke.server.mix.MixMain._
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger

/**
  * Created by Administrator on 2017/1/18.
  */
class MixRec {

  val logger = Logger.getLogger(getClass)
  MixConstant.mapGraph
  val dao = new DaoImpl
  val service = new MixServerImpl()
  val VideoMap =redisSave

  def runAllMix(): Unit = {
    // 全量分析
    while (true) {
      val users = dao.queryAllUserId()
      for (u <- users) {
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
      Thread.sleep(ConfigMix.time)
    }
  }

}
