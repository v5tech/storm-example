package net.aimeizi.example.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.Random;

/**
 * Created by Administrator on 2015/12/19 0019.
 */
public class KafkaProducer {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        // 集群环境 metadata.broker.list 为kafka config/server.properties文件中host.name属性的值 host:port 集群环境中为多个server.properties文件中host.name属性的值
        props.put("metadata.broker.list", "192.168.64.128:9092,192.168.64.129:9092,192.168.64.131:9092");
        // 单机环境
//        props.put("metadata.broker.list", "192.168.0.201:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);

        String words = "Each partition is an ordered immutable sequence of messages that is continually appended to—a commit log The messages in the partitions are each assigned a sequential id number called the offset that uniquely identifies each message within the partition" +
                "The Kafka cluster retains all published messages—whether or not they have been consumed—for a configurable period of time For example if the log retention is set to two days then for the two days after a message is published it is available for consumption after which it will be discarded to free up space Kafka's performance is effectively constant with respect to data size so retaining lots of data is not a problem" +
                "In fact the only metadata retained on a per-consumer basis is the position of the consumer in the log called the offset This offset is controlled by the consumer: normally a consumer will advance its offset linearly as it reads messages but in fact the position is controlled by the consumer and it can consume messages in any order it likes For example a consumer can reset to an older offset to reprocess" +
                "This combination of features means that Kafka consumers are very cheap—they can come and go without much impact on the cluster or on other consumers For example you can use our command line tools to tail the contents of any topic without changing what is consumed by any existing consumers" +
                "The partitions in the log serve several purposes First they allow the log to scale beyond a size that will fit on a single server Each individual partition must fit on the servers that host it but a topic may have many partitions so it can handle an arbitrary amount of data Second they act as the unit of parallelism—more on that in a bit" +
                "The partitions of the log are distributed over the servers in the Kafka cluster with each server handling data and requests for a share of the partitions Each partition is replicated across a configurable number of servers for fault tolerance" +
                "Each partition has one server which acts as the leader and zero or more servers which act as followers The leader handles all read and write requests for the partition while the followers passively replicate the leader If the leader fails one of the followers will automatically become the new leader Each server acts as a leader for some of its partitions and a follower for others so load is well balanced within the cluster" +
                "Producers publish data to the topics of their choice The producer is responsible for choosing which message to assign to which partition within the topic This can be done in a round-robin fashion simply to balance load or it can be done according to some semantic partition function say based on some key in the message More on the use of partitioning in a second" +
                "Messaging traditionally has two models: queuing and publish-subscribe In a queue a pool of consumers may read from a server and each message goes to one of them; in publish-subscribe the message is broadcast to all consumers Kafka offers a single consumer abstraction that generalizes both of these—the consumer group" +
                "Consumers label themselves with a consumer group name and each message published to a topic is delivered to one consumer instance within each subscribing consumer group Consumer instances can be in separate processes or on separate machines" +
                "If all the consumer instances have the same consumer group then this works just like a traditional queue balancing load over the consumers" +
                "If all the consumer instances have different consumer groups then this works like publish-subscribe and all messages are broadcast to all consumers" +
                "More commonly however we have found that topics have a small number of consumer groups one for each logical subscriber Each group is composed of many consumer instances for scalability and fault tolerance This is nothing more than publish-subscribe semantics where the subscriber is cluster of consumers instead of a single process";
        String[] w = words.split(" ");
        // 发送业务消息
        for (int i = 1; i <= 10000; i++) {
            Thread.sleep(500);
            Random random = new Random();
            int n = random.nextInt(w.length);
            KeyedMessage<String, String> message = new KeyedMessage<String, String>("kafka-storm", w[n], w[n]);
            producer.send(message);
        }
    }
}
