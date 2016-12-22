package com.scala.kanke.server.hot.server

import com.java.kanke.utils.bean.Video
import com.java.kanke.utils.redis.JedisUtil
import com.scala.kanke.server.hot.service.DaoServer
import com.scala.kanke.server.hot.service.impl.CalculateImpl

/**
  * Created by Administrator on 2016/11/8.
  */
class Server {


  def server(daoServer:DaoServer): Unit ={
    //取数据
    val sql: String = "SELECT t.vod_id as id,  t.video_type as videotype, " +
      " t.play_Count as playcount " +
//      " t.play_count_monthy as monthcount," +
//      " t.play_count_weekly as weekcount " +
     "  FROM t_vod_combine t WHERE  T.is_matched=1  order by play_Count desc "

    val videolist = daoServer.queryVideo(sql,null,classOf[Video]);
    println(videolist.size)

    //统计分析
    val cal = new CalculateImpl
    val map = cal.statistics(videolist)
    println(map)
    // 保存redis
    redisSavaData(map)
  }


  def redisSavaData(map: Map[String, Seq[String]]): Unit ={
    val jedisUtil = JedisUtil.getInstance;
    val jedis = jedisUtil.getJedis
    map.map(x=>{
      val key = x._1
      x._2.map(y => jedis.hset(,key,y+";"))
      key
    })
    jedisUtil.returnJedis(jedis)
  }
}
