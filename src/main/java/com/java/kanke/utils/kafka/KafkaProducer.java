package com.java.kanke.utils.kafka;

import com.google.gson.Gson;
import com.java.kanke.utils.mysql.DBCommon;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class KafkaProducer 
{
    private final Producer<String, String> producer;
//    private final org.apache.kafka.clients.producer.KafkaProducer producer2;
    public final static String TOPIC = "test";

    private KafkaProducer(){
        Properties props = new Properties();
//        props.put("metadata.broker.list", "121.42.141.232:9092,115.28.156.126:9092,121.42.60.39:9092");
        props.put("metadata.broker.list", "122.193.13.70:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");

        //request.required.acks
        //0, which means that the producer never waits for an acknowledgement from the broker (the same behavior as 0.7). This option provides the lowest latency but the weakest durability guarantees (some data will be lost when a server fails).
        //1, which means that the producer gets an acknowledgement after the leader replica has received the data. This option provides better durability as the client waits until the server acknowledges the request as successful (only messages that were written to the now-dead leader but not yet replicated will be lost).
        //-1, which means that the producer gets an acknowledgement after all in-sync replicas have received the data. This option provides the best durability, we guarantee that no messages will be lost as long as at least one in sync replica remains.
        props.put("request.required.acks","-1");



        producer = new Producer<String, String>(new ProducerConfig(props));

//        Properties props2 = new Properties();
//        props2.put("bootstrap.servers","localhost:9092");
//        props2.put("acks", "all");
//        props2.put("key.serializer", "kafka.serializer.StringEncoder");
//        props2.put("value.serializer", "kafka.serializer.StringEncoder");
//        producer2 =new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props2);
    }
//    void produce() {
//        int messageNo = 1;
//        final int COUNT = 4000;
//        while (messageNo < COUNT) {
//            String key = String.valueOf(messageNo);
//            String data = "hello kafka message " + key;
//            producer.send(new KeyedMessage<String, String>(TOPIC, key ,data));
//            System.out.println(data);
//            messageNo ++;
//        }
//    }


//    void produce2(String json ){
//        ProducerRecord producerRecord = new ProducerRecord("test", "messageKey", json);
//        producer2.send(producerRecord);
//        producer2.close() ;
//    }

        void produce1(String json ){
        KeyedMessage keyedMessage = new KeyedMessage("test", "messageKey", json);
        producer.send(keyedMessage);
//        producer.close() ;
    }

    public static void main( String[] args )
    {
        KafkaProducer kafkaProducer = new KafkaProducer();
//        new KafkaProducer().produce()
        List<TestBean> tlist = kafkaProducer.testSaveBean();
        for(TestBean t:tlist){
            Gson gson = new Gson();
            kafkaProducer.produce1("456");
        }

        System.out.println("完成！");
    }



    public <T> List<T> testSaveBean(){

        String sql = "select id , userid  ,vodid as videoid ,kankeid from t_userhistory where id > ? order by id asc" ;
        Object[] params = new Object[1];
        params[0]=0;
        List<T> listbean = DBCommon.queryForBean(sql,params,TestBean.class );
        return listbean;
    }

}