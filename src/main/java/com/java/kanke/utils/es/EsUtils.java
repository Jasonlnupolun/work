package com.java.kanke.utils.es;

import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.nlpcn.es4sql.SearchDao;
import org.nlpcn.es4sql.exception.SqlParseException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLFeatureNotSupportedException;

public class EsUtils {
	private Client client ;
	private static SearchDao searchDao;
	public EsUtils() throws UnknownHostException{
		  Settings settings = Settings  
		            .settingsBuilder()  
//		            .put("cluster.name","kanke-cluster")
				.put("cluster.name","kanke-cluster")
				.put("client.transport.sniff", true)
		            .build();
		InetSocketTransportAddress[] listTransportAddress = getTransportAddress();
		client = TransportClient.builder()
//				.addPlugin(DeleteByQueryPlugin.class)
				.settings(settings).build()
				.addTransportAddress(listTransportAddress[0]);
//				.addTransportAddress(listTransportAddress[1])
//				.addTransportAddress(listTransportAddress[2]);
        NodesInfoResponse nodeInfos = client.admin().cluster().prepareNodesInfo().get();
		String clusterName = nodeInfos.getClusterName().value();
		System.out.println(String.format("Found cluster... cluster name: %s", clusterName));

//     		// Load test data.
//             if(client.admin().indices().prepareExists("test").execute().actionGet().isExists()){
//                 client.admin().indices().prepareDelete("test").get();
//             }

	}
	
	public void testAggregation(){
		
	}

	public Client getClient(){
		return this.client;
	}




	public static void main(String[] args) throws UnknownHostException, SQLFeatureNotSupportedException, SqlParseException {
		EsUtils es = new EsUtils() ;
        NodesInfoResponse nodeInfos = es.client.admin().cluster().prepareNodesInfo().get();
		String clusterName = nodeInfos.getClusterName().value();
		System.out.println(String.format("Found cluster... cluster name: %s", clusterName));
        searchDao = new SearchDao(es.client);
        searchDao.explain("delete from test1");
        //refresh to make sure all the docs will return on queries
		System.out.println("Finished the setup process...");
	}
	private static InetSocketTransportAddress[] getTransportAddress() throws UnknownHostException {
//		String host1 = "115.28.156.126";
		String host1 = "127.0.0.1";
		String port1 = "9300";
//		String host2 = "115.28.156.126" ;
//		String port2 = "9300" ;
//		String host3 = "121.42.60.39" ;
//		String port3 = "9300" ;
		InetSocketTransportAddress[] inetSocketTransportAddressList = new InetSocketTransportAddress[3];
		inetSocketTransportAddressList[0]=new InetSocketTransportAddress(InetAddress.getByName(host1), Integer.parseInt(port1)) ;
//		inetSocketTransportAddressList[1]=new InetSocketTransportAddress(InetAddress.getByName(host2), Integer.parseInt(port2)) ;
//		inetSocketTransportAddressList[2]=new InetSocketTransportAddress(InetAddress.getByName(host3), Integer.parseInt(port3)) ;
		return inetSocketTransportAddressList;
	}
	
}
