package com.java.kanke.utils.kafka;

import com.scala.kanke.common.ConfigMix;
import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaConsumer {

    private final ConsumerConnector consumer;
    public final static String TOPIC = ConfigMix.topic();
    public KafkaConsumer() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "122.193.13.70:2181");
        props.put("zookeeper.session.timeout.ms", "80000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("group.id", "top1");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("bootstrap.servers", "122.193.13.70:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("auto.offset.reset", "smallest");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        ConsumerConfig config = new ConsumerConfig(props);
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }

    public KafkaConsumer(String test){
        Properties props = new Properties();
        props.put("bootstrap.servers", "122.193.13.70:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer2 = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(props);
        consumer2.subscribe(Arrays.asList("my-topic"));
        while (true) {
            ConsumerRecords<String, String> records = consumer2.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }

    public ConsumerIterator<String, String> consume() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, new Integer(1));
        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
        Map<String, List<KafkaStream<String, String>>> consumerMap =
                consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
        KafkaStream<String, String> stream = consumerMap.get(TOPIC).get(0);
        ConsumerIterator<String, String> it = stream.iterator();
        return it;
    }
    public void muliCommonConsumer(){
        Properties props = new Properties();
        props.put("zk.connect", "192.168.181.128:2181");
        props.put("zk.connectiontimeout.ms", "1000000");
        props.put("groupid", "test_group");
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, Integer> map=new HashMap<String,Integer>();
        map.put(TOPIC, 2);
        // create 4 partitions of the stream for topic “test”, to allow 4 threads to consume
        Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams =
                consumerConnector.createMessageStreams(map);
        List<KafkaStream<byte[], byte[]>> streams = topicMessageStreams.get(TOPIC);
        // create list of 4 threads to consume from each of the partitions
        ExecutorService executor = Executors.newFixedThreadPool(4);
        // consume the messages in the threads
        for(final KafkaStream<byte[], byte[]> stream: streams) {
            executor.submit(new Runnable() {
                public void run() {
                    for(MessageAndMetadata<byte[], byte[]> msgAndMetadata: stream) {
                        // process message (msgAndMetadata.message())
                        try {
                            System.out.println(new String(msgAndMetadata.message(), "utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    public void consumer2(){
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "122.193.13.70:2181");
        properties.put("auto.commit.enable", "true");
        properties.put("auto.commit.interval.ms", "60000");
        properties.put("group.id", "asdfas");
        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        ConsumerConnector javaConsumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        //topic的过滤器
        Whitelist whitelist = new Whitelist(TOPIC);
        List<KafkaStream<byte[], byte[]>> partitions = javaConsumerConnector.createMessageStreamsByFilter(whitelist);
        if (CollectionUtils.isEmpty(partitions)) {
            System.out.println("empty!");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //消费消息
        for (KafkaStream<byte[], byte[]> partition : partitions) {

            ConsumerIterator<byte[], byte[]> iterator = partition.iterator();
            while (iterator.hasNext()) {
                MessageAndMetadata<byte[], byte[]> next = iterator.next();
                System.out.println("partiton:" + next.partition());
                System.out.println("offset:" + next.offset());
                try {
                    System.out.println("message:" + new String(next.message(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static void main(String[] args) {

        KafkaConsumer kafkaConsumer = new KafkaConsumer();
//        kafkaConsumer.consumer2();
        ConsumerIterator<String, String> it = kafkaConsumer.consume();

        while (it.hasNext()) {
            System.out.println(it.next().message());
        }
//        new KafkaConsumer().consumer2();
    }
}