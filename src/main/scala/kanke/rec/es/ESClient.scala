package kanke.rec.es

import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson.Gson
import org.apache.log4j.Logger
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.NoNodeAvailableException
import org.elasticsearch.search.SearchHit
import org.nlpcn.es4sql.SearchDao

import scala.collection.JavaConversions

/**
  * Created by gavin on 16-4-12.
  * northland89@163.com 
  */
case class ESClient(client:Client) {
  val logger = Logger.getLogger(getClass)

  var searchDao = new SearchDao(client)


  def query[T](eSJob:ESJob[T],classType:Class[T]): Seq[T] ={
    val sql = eSJob.getSql
    val select = searchDao.explain(sql).explain()
    try {
      val result = select.get().asInstanceOf[SearchResponse].getHits
      val hits: Iterable[SearchHit] = JavaConversions.iterableAsScalaIterable(result)
      hits.map(unit => {
        val bean: T = eSJob.jsonToBean[T](unit.getSourceAsString, classType)
        bean
      }).toSeq
    }catch {
      case e:NoNodeAvailableException =>logger.error(e);Seq[T]()
      case any:Throwable=>logger.error(any);throw any
    }
  }
  def close(): Unit ={
    if(null != client){
      try {
        client.close();
      } catch  {
        case e:Exception => logger.error(e)
      }
    }

  }

  def sava[T](index:String,name:String,bean:T): Unit ={
    try{
      client.prepareIndex(index, name).setSource(new Gson().toJson(bean)).get
    }catch {
      case e:NoNodeAvailableException =>{
        logger.error(e)
      };
      case any:Throwable=>logger.error(any);throw any
    }

  }

}


object ESClient{
  def apply(host: String, port: Int,clusterName:String="elasticsearch",isSniff:Boolean=true): ESClient ={
    val logger = Logger.getLogger(getClass)
    logger.info(s"$host $port $clusterName")
    val client = EsUtils.getClient(host ,port,clusterName)
    ESClient(client)
  }

  case class Test(userid:String,kankeid:String,year:Int,score:Double,tags:String,region:String,videotype:String,playcount:Int,sources:String,addtime: String,flag:String="")
  case class EPG(userid:String,channelid:String,kankeid:String,year:Int,score:Double,tags:String,region:String,videotype:String,playcount:Int,sources:String,addtime: String,flag:String="")
  def main(args: Array[String]) {
    import com.scala.kanke.common._
    var format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//    ESClient( elasticip,elasticport,clustername).sava("testq","lsy",EPG("1","47840","4103",2015,8.6,"爱情;言情;偶像;年代;","泰国","tv",57600,"1",format.format(new Date())))
    ESClient( elasticip,elasticport,clustername).sava("wechat2.0","kafka",Test("1","4103",2015,8.6,"爱情;言情;偶像;年代;","泰国","tv",57600,"1",format.format(new Date())))
  }
}
