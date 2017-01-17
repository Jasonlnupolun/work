package kanke.rec.es

import org.apache.log4j.Logger
import org.elasticsearch.action.search.{SearchResponse, SearchRequestBuilder}
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.NoNodeAvailableException
import org.elasticsearch.search.SearchHit
import org.nlpcn.es4sql.SearchDao
import org.nlpcn.es4sql.query.SqlElasticRequestBuilder
import scala.collection.JavaConversions

/**
  * Created by gavin on 16-4-12.
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
    client.close()
  }
}


object ESClient{
  def apply(host: String, port: Int,clusterName:String="elasticsearch",isSniff:Boolean=true): ESClient ={
    val logger = Logger.getLogger(getClass)
    logger.info(s"$host $port $clusterName")
    val client = EsUtils.getClient("localhost",9200)
    ESClient(client)
  }
}
