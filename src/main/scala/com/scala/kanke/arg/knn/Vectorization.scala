package com.scala.kanke.arg.knn

import breeze.linalg.DenseVector
import com.java.kanke.utils.bean.Video
import com.scala.kanke.arg.canopy.VideoVector

/**
  * Created by Administrator on 2016/11/11.
  */
trait Vectorization {
  def vectorizer():List[FeatureBean]
}

class Vectoriza(videos: List[Video],coordinates:Array[String]) extends Vectorization{
  //每一个Video转换为特征的FeatureBean
  def vectorizer():List[FeatureBean]={
    videos.map(transfer)
  }


  def transfer(metaSourceBean: Video): FeatureBean ={
    var featureBean = new FeatureBean
    val tagsVector = DenseVector.zeros[Double](coordinates.size)
    var k = 0
    if(metaSourceBean.getTags!=null){
      // 向量化标签
      for(i<- metaSourceBean.getTags.split(";")){
        val index = coordinates.indexOf(i)
        if(index> -1){
          if(k<2 ) tagsVector(index) = 1.4
          else tagsVector(index) = 0.8
        }
        k=k+1
      }
    }
    if(metaSourceBean.getRegion!=null) {
      //向量化地区
      for (i <- metaSourceBean.getRegion.split(";")) {
        val index = coordinates.indexOf(i)
        if(index> -1 )tagsVector(index) = 2
      }
    }
    //向量化年份

    //向量化播放量
    featureBean.setId(metaSourceBean.getId)
    featureBean.setKankeid(metaSourceBean.getKankeid)
    featureBean.setVideoid(metaSourceBean.getVideoid)
    featureBean.setChannelid(metaSourceBean.getChannelid)
    featureBean.setTags(tagsVector)
    featureBean.setRegion(tagsVector)
    featureBean.setYear(0)
    featureBean.setPlaycount(0)
    featureBean
  }


}

object Vectoriza {
  implicit def featureBeanToVideoVector(featureBean: FeatureBean):VideoVector={
    var videoVector =new VideoVector(featureBean.getKankeid,featureBean.getTags)
    videoVector.setTags(featureBean.getTagsString.split(";"))
    videoVector
  }


  def transferFeatureBean(metaSourceBean: Video,coordinates:Array[String]): FeatureBean ={

    var featureBean = new FeatureBean
    val tagsVector = DenseVector.zeros[Double](coordinates.size)
    var k = 0
    if(metaSourceBean!=null && metaSourceBean.getTags!=null){
      // 向量化标签
      for(i<- metaSourceBean.getTags.split(";")){
        val index = coordinates.indexOf(i)
        if(index> -1){
          if(k<2 ) tagsVector(index) = 2
          else tagsVector(index) = 1
        }
        k=k+1
      }
    }
    if(metaSourceBean!=null && metaSourceBean.getRegion!=null) {
      //向量化地区
      for (i <- metaSourceBean.getRegion.split(";")) {
        val index = coordinates.indexOf(i)
        if(index> -1 )tagsVector(index) = 2
      }
    }

//    向量化类型
    if(metaSourceBean!=null && metaSourceBean.getVideotype!=null) {
        val index = coordinates.indexOf(metaSourceBean.getVideotype)
        if(index> -1 )tagsVector(index) = 8

    }
    //向量化年份

    //向量化播放量
    featureBean.setId(metaSourceBean.getId)
    featureBean.setTagsString(metaSourceBean.getTags)
    featureBean.setKankeid(metaSourceBean.getKankeid)
    featureBean.setVideoid(metaSourceBean.getVideoid)
    featureBean.setChannelid(metaSourceBean.getChannelid)
    featureBean.setTags(tagsVector)
    featureBean.setRegion(tagsVector)
    featureBean.setYear(0)
    featureBean.setPlaycount(0)
    featureBean
  }
}