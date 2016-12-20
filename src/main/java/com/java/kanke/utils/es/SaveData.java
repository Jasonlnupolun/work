package com.java.kanke.utils.es;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/11/30.
 */
public class SaveData {
    private static Client client = null ;
    static{
        try {
            client = new EsUtils().getClient();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void  saveData(String json){
        IndexResponse response = client.prepareIndex("twitter", "tweet")
                //必须为对象单独指定ID
                .setId("1")
                .setSource(json)
                .execute()
                .actionGet();
        //多次index这个版本号会变
        System.out.println("response.version():"+response.getVersion());
        client.close();
    }

    public static void deleteDate(String json){
        //在这里创建我们要索引的对象
        DeleteResponse response = client.prepareDelete("twitter", "tweet", "1")
                .execute().actionGet();
    }
}

