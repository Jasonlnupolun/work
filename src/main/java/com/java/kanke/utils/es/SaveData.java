package com.java.kanke.utils.es;

import com.google.gson.Gson;
import com.scala.kanke.arg.graphcluster.KmResults;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
public class SaveData {
    private static Client client = null ;
    static{
        try {
            client = getSingleClient("115.28.156.126");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static Client getSingleClient(String ip) throws UnknownHostException {
        Settings settings = Settings
                .settingsBuilder()
                .put("cluster.name","kanke-cluster")
//				.put("cluster.name","elasticsearch")
                .put("client.transport.sniff", true)
                .build();
        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), 9300));
        return client;
    }
    public static void  saveData(List<KmResults> json,String index,String name ){
        for(KmResults k:json){
             client.prepareIndex(index, name).setSource(new Gson().toJson(k)).get();
        }
    }

    public static void bulkSave(List<KmResults> json, String index, String name ){
        BulkRequestBuilder bulkRequest=client.prepareBulk();
        int count=0;
        if(json!=null){
            for(KmResults k:json)
            bulkRequest.add(client.prepareIndex(index,name).setSource(new Gson().toJson(k)));
            if (count%10==0) {
                bulkRequest.execute().actionGet();
            }
            count++;
        }
        bulkRequest.execute().actionGet();
    }

    public static void deleteDate(String json){
        //在这里创建我们要索引的对象
//        DeleteResponse response = client.prepareDelete("data", "log", "1")
//                .execute().actionGet();
        client.admin().indices().prepareDelete(json)
                .execute().actionGet();
    }

    public static void  saveDataDemo(String json,String id){
        IndexResponse response = client.prepareIndex(id,"data", "log")
                //必须为对象单独指定ID
                .setId("1")
                .setSource(json)
                .execute()
                .actionGet();
        //多次index这个版本号会变
        System.out.println("response.version():"+response.getVersion());
        client.close();
    }




    public static void main(String[] args) {
        deleteDate("jsyx");

    }

    public static void test(){

        try {
            /* 创建客户端 */
            // client startup
            Client client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
            KmResults rlatek = new KmResults();
            rlatek.setId("1");
            rlatek.setTags("2");
            IndexResponse response = client.prepareIndex("jsyx", "kmeans").setSource(new Gson().toJson(rlatek)).get();
            if (response.isCreated()) {
                System.out.println("创建成功!");
            }
            client.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

