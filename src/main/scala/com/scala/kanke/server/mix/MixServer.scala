package com.scala.kanke.server.mix

import com.java.kanke.utils.bean.UserHistory
import com.scala.kanke.arg.canopy.{CanopyBuilder, VideoVector, Canopy}
import com.scala.kanke.arg.knn.Knn.Result
import com.scala.kanke.arg.knn.{FeatureBean, Knn, Vectoriza}
import com.scala.kanke.common.{MixConstant, Constant}
import com.scala.kanke.service.{ClassServiceImpl, UserHistoryService}
import com.scala.kanke.utils.Jedis
import org.apache.log4j.Logger
import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer
/**
  * Created by Administrator on 2016/11/16.
  */
trait MixServer

class MixServerImpl {
      val logger = Logger.getLogger(getClass)
      val userHistoryService = new UserHistoryService()

      @BeanProperty var historyid = Set[String]()
      def startServerMix(bean:UserHistory): Unit ={
        //根据用户历史  查全信息量
        val listvideo = userHistoryService.userHistoryToVideo(bean)
        //构建特征向量
        val beanFeature =  new Vectoriza(listvideo,Constant.coordinate).vectorizer()(0)
        //查询用户历史记录
//        val eSClient =  ESClient(tc.esIp,tc.esPort,tc.esClusterName,tc.esIsSniff)
//        val mixUserRecord = eSClient.query(QueryITUMixJob(uid, 50, 60, config.esIndex), classOf[UserHistory])
        val userhistoryids = Jedis.getHistoryVideoIds(bean.getUserid,bean.getVideoid)
        // 取用户历史纪录的代表
        // 根据代表找相似的影片
        val caseids = Knn.searchIds(beanFeature,MixConstant.mapGraph("tv"),10)
        logger.info("查找到的相似影片："+caseids)
//        //去重复
//        val historysets =""
        //上次观看历史放入数据库的值
        val  userRedisids= Jedis.getMixJedis(bean.getUserid)
        val  redisMap = scala.collection.mutable.Map[String,Double]()
        val  userids = userRedisids.map(x=>{
          val a = x.split(":")
          redisMap(a(0))=a(1).toDouble
          a(0)
        })
        //去重复
        //得到推荐的结果Map id->权重
        val knnMap = scala.collection.mutable.Map[String,Double]()
        val ids = caseids.map(x=> {
          knnMap(x.id)=x.weight
          x.id
        })

//        推荐的影片去掉redis库中已有的数据
        val distinct = ids.diff(userids)
        logger.info("去掉重复的ids"+distinct)
        // 新的推荐结果添加到redis已有的库中
        val integration = userids ++:distinct
        //过滤掉观看过的历史数据
        logger.info("观看历史记录"+userhistoryids)
        val jedis = integration.filter(_!=())
        val resultJedis =jedis.diff(userhistoryids.toArray[String])
        //构建推荐排序
        val res = resultJedis.reduceLeft((x, y)=> x+";"+y)
        Jedis.putJedis("tv",bean.getUserid,res.toString)

        logger.info("原来的推荐是："+ userids)
        logger.info("保存结果是："+res)
      }


  // 定时启动全量分析
  def startAllServerMix(typename:String,bean:List[UserHistory]): scala.collection.mutable.Map[String,Double] ={
    //根据用户历史  查全信息量  //构建特征向量
    val listvideo = userHistoryService.userHistoryToVideo(bean)

    //查询用户历史记录   bean 即是历史数据  构建看过影片的id集合
    val historyidSet = bean.map(x=>x.getKankeid).toSet[String]
    //        val eSClient =  ESClient(tc.esIp,tc.esPort,tc.esClusterName,tc.esIsSniff)
    //        val mixUserRecord = eSClient.query(QueryITUMixJob(uid, 50, 60, config.esIndex), classOf[UserHistory])
    // 取用户历史纪录的代表
    val canopys = doCanopy(listvideo.toList)
    //得到推荐的结果Map id->权重  //推荐有重复的，进行去重复
    val knnMap = scala.collection.mutable.Map[String,Double]()
    for(i<-canopys) {
      val k = i.points.size * 10
      val caseids = Knn.searchIdsByCanopy(i, MixConstant.mapGraph(typename), k)
      //添加权重  &  过滤掉观看过的历史数据
      for (ca <- caseids if !historyidSet.contains(ca.id)) {
        //设置影片的权重，公式为相似度×（1+簇权重）×（1+年代权重×年代）
        val tmpid = ca.id
        val tmptypename = tmpid.substring(0,tmpid.indexOf("_"))
        val array = Jedis.getMixJedis(tmptypename,ca.id)
        if(array!=null){
          val tmpArray = array.split(";");
          var weight = 0.0
          val similarity = ca.weight;
          val canopyweight = ca.weight * (1+i.points.size/listvideo.size)      //根据聚类的个数设置影片相似权重
          val yearweight = tmpArray(0).toDouble
          val playcount = tmpArray(1)
          val score = tmpArray(2)
          weight = 1.0 * canopyweight + 2.0 * yearweight
          if(knnMap.get(ca.id)== None || knnMap(ca.id) < canopyweight){
            knnMap(ca.id)=weight
          }
        }else{
          var weight = 0.0
          val similarity = ca.weight;
          val canopyweight = ca.weight * (1+i.points.size/listvideo.size)      //根据聚类的个数设置影片相似权重
          val yearweight = 0
          val playcount = 0
          val score = 0
          weight = 1.0 * canopyweight + 2.0 * yearweight
          if(knnMap.get(ca.id)== None || knnMap(ca.id) < canopyweight){
            knnMap(ca.id)=weight
          }
        }
      }
    }

    logger.info("保存结果是："+knnMap)
    knnMap
  }


  def doCanopy(featureBeans:List[FeatureBean]):ArrayBuffer[Canopy]={
    var pointsBuffer = ArrayBuffer[VideoVector]()
    val buffer =for (i <- featureBeans if i!=null){
      pointsBuffer += Vectoriza.featureBeanToVideoVector(i)
    }
    CanopyBuilder.points = pointsBuffer
    CanopyBuilder.canopies =ArrayBuffer[Canopy]()
    CanopyBuilder.run
    CanopyBuilder.canopies
  }



}
