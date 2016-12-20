package com.java.kanke.utils.es;

import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.plugin.deletebyquery.DeleteByQueryPlugin;
import org.nlpcn.es4sql.SearchDao;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EsUtilsSql {

	
	private static TransportClient client;
	private static SearchDao searchDao;
	public  EsUtilsSql() throws Exception {
		 Settings settings = Settings  
		            .settingsBuilder()  
		            .put("cluster.name","kanke-cluster")
		            .put("client.transport.sniff", true)  
		            .build(); 
        client = TransportClient.builder().addPlugin(DeleteByQueryPlugin.class).settings(settings).build().addTransportAddress(getTransportAddress());
        NodesInfoResponse nodeInfos = client.admin().cluster().prepareNodesInfo().get();
		String clusterName = nodeInfos.getClusterName().value();
		System.out.println(String.format("Found cluster... cluster name: %s", clusterName));

        searchDao = new SearchDao(client);
        //refresh to make sure all the docs will return on queries

		System.out.println("Finished the setup process...");
	}
	
	
	public static SearchDao getSearchDao() {
		return searchDao;
	}

	public static TransportClient getClient() {
		return client;
	}

	private static InetSocketTransportAddress getTransportAddress() throws UnknownHostException {
		String host = System.getenv("ES_TEST_HOST");
		String port = System.getenv("ES_TEST_PORT");

		if(host == null) {
//			host = "localhost";
			host = "183.136.202.151";
			System.out.println("ES_TEST_HOST enviroment variable does not exist. choose default 'localhost'");
		}

		if(port == null) {
			port = "9300";
			System.out.println("ES_TEST_PORT enviroment variable does not exist. choose default '9300'");
		}

		System.out.println(String.format("Connection details: host: %s. port:%s.", host, port));
		return new InetSocketTransportAddress(InetAddress.getByName(host), Integer.parseInt(port));
	}
	
	
	
}
