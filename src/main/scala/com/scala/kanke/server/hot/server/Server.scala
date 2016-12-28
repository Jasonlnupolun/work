package com.scala.kanke.server.hot.server

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.redis.JedisUtil
import com.scala.kanke.common.ConfigHot
import com.scala.kanke.server.hot.service.DaoServer
import com.scala.kanke.server.hot.service.impl.CalculateImpl

/**
  * Created by Administrator on 2016/11/8.
  */
class Server {


  def server(daoServer:DaoServer): Unit ={
    //取数据
    val sql: String =ConfigHot.popsql

    val videolist = daoServer.queryVideo(sql,null,classOf[Video]);

    //统计分析
    val cal = new CalculateImpl
    val map = cal.statistics(videolist)
    // 保存redis
    redisSavaData(map)
  }


  def redisSavaData(map: Map[String, Seq[String]]): Unit ={
    val jedisUtil = JedisUtil.getInstance;
    val jedis = jedisUtil.getJedis
    for((k,v)<-map){
      val value = v.mkString(";")
      jedis.hset(ConfigHot.prefixup,k,value)
    }
    jedisUtil.returnJedis(jedis)
  }
}
