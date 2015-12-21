package net.aimeizi.example.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2015/12/19 0019.
 */
public class KafkaConsumer {
    private static final String topic = "kafka-storm";
    private static final Integer threads = 1;

    public static void main(String[] args) {

        Properties props = new Properties();
        // kafka集群环境zookeeper集群主机地址 即kafka集群环境server.properties配置文件中zookeeper.connect=s1:2181,s2:2181,s3:2181
        props.put("zookeeper.connect", "192.168.64.128:2181,192.168.64.129:2181,192.168.64.130:2181");
//        props.put("zookeeper.connect", "192.168.0.201:2181");
        props.put("group.id", "mygroup");
//        props.put("auto.offset.reset", "smallest");
        props.put("zookeeper.session.timeout.ms", "400000");

        ConsumerConfig config = new ConsumerConfig(props);
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(config);
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, threads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        for (final KafkaStream<byte[], byte[]> kafkaStream : streams) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (MessageAndMetadata<byte[], byte[]> mm : kafkaStream) {
                        String msg = new String(mm.message());
                        System.out.println(msg);
                    }
                }
            }).start();
        }
    }
}
