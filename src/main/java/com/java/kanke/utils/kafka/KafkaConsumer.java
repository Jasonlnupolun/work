package com.java.kanke.utils.kafka;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaConsumer {

    private final ConsumerConnector consumer;
    public final static String TOPIC = "test";
    public KafkaConsumer() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "122.193.13.70:2181");
//        props.put("zookeeper.connect", "127.0.0.1:2181");
        props.put("group.id", "jd-group");
        props.put("zookeeper.session.timeout.ms", "4000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ConsumerConfig config = new ConsumerConfig(props);
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
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
//        while (it.hasNext())
//            System.out.println(it.next().message());
    }

    public static void main(String[] args) {
        ConsumerIterator<String,String> it = new KafkaConsumer().consume();
        while (it.hasNext())
            System.out.println(it.next().message());
    }
}