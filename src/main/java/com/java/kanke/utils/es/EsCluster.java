package com.java.kanke.utils.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class EsCluster {

	 public static final String CLUSTER_NAME = "kanke-cluster"; //实例名称
	    private static final String IP1 = "121.42.141.232";
	    private static final int PORT1 = 9300;  //端口
		private static final String IP2 = "115.28.156.126";
		private static final int PORT2 = 9300;  //端口
		private static final String IP3 = "121.42.60.39";
		private static final int PORT3 = 9300;  //端口
	    //1.设置集群名称：默认是elasticsearch，并设置client.transport.sniff为true，使客户端嗅探整个集群状态，把集群中的其他机器IP加入到客户端中  
	    /* 
	    //对ES1.6有效 
	    private static Settings settings = ImmutableSettings 
	            .settingsBuilder() 
	            .put("cluster.name",CLUSTER_NAME) 
	            .put("client.transport.sniff", true) 
	            .build(); 
	    */  
	    //对ES2.0有效  
	    private static Settings settings = Settings  
	            .settingsBuilder()  
	            .put("cluster.name",CLUSTER_NAME)  
	            .put("client.transport.sniff", true)  
	            .build();  
	    //创建私有对象  
	    private static TransportClient client;  
	  
	    //反射机制创建单例的TransportClient对象  ES1.6版本  
//	    static {  
//	        try {  
//	            Class<?> clazz = Class.forName(TransportClient.class.getName());  
//	            Constructor<?> constructor = clazz.getDeclaredConstructor(Settings.class);  
//	            constructor.setAccessible(true);  
//	            client = (TransportClient) constructor.newInstance(settings);  
//	            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP), PORT));  
//	        } catch (Exception e) {  
//	            e.printStackTrace();  
//	        }  
//	    }  
	  
	    //ES2.0版本  
	    static {  
	        try {
	            client = TransportClient.builder().settings(settings).build()  
	                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP1), PORT1))
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP2), PORT2))
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(IP3), PORT3));
	        } catch (UnknownHostException e) {  
	            e.printStackTrace();  
	        }  
	    }  
}
