package kanke.rec.es

import java.net.{InetAddress, UnknownHostException}

import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.plugin.deletebyquery.DeleteByQueryPlugin


/**
  */
object EsUtils {
  def getClient(localhost: String, port: Int,clusterName:String="elasticsearch"): Client = {
    val settings = Settings.settingsBuilder
      .put("cluster.name", clusterName)
      .put("client.transport.sniff", true).build;
    val listTransportAddress = getLocalTransportAddress

    TransportClient.builder.settings(settings)
      .build.addTransportAddress(listTransportAddress(0))
//      .addTransportAddress(listTransportAddress(1))
//      .addTransportAddress(listTransportAddress(2))
  }

  @throws(classOf[UnknownHostException])
  private def getTransportAddress: Array[InetSocketTransportAddress] = {
    val host1: String = "121.42.141.232"
    val port1: String = "9300"
    val host2: String = "115.28.156.126"
    val port2: String = "19300"
    val host3: String = "121.42.60.39"
    val port3: String = "9300"
    val inetSocketTransportAddressList: Array[InetSocketTransportAddress] = new Array[InetSocketTransportAddress](3)
    inetSocketTransportAddressList(0) = new InetSocketTransportAddress(InetAddress.getByName(host1), port1.toInt)
    inetSocketTransportAddressList(1) = new InetSocketTransportAddress(InetAddress.getByName(host2), port2.toInt)
    inetSocketTransportAddressList(2) = new InetSocketTransportAddress(InetAddress.getByName(host3), port3.toInt)
    inetSocketTransportAddressList
  }

  @throws(classOf[UnknownHostException])
  private def getLocalTransportAddress: Array[InetSocketTransportAddress] = {
    val host1: String = "127.0.0.1"
    val port1: String = "9300"
    val inetSocketTransportAddressList: Array[InetSocketTransportAddress] = new Array[InetSocketTransportAddress](1)
    inetSocketTransportAddressList(0) = new InetSocketTransportAddress(InetAddress.getByName(host1), port1.toInt)

    inetSocketTransportAddressList
  }

  def getESClient(localhost: String, port: Int,clusterName:String="elasticsearch",isSniff:Boolean=true):ESClient={
    ESClient(getClient(localhost,port,clusterName))
  }








}

