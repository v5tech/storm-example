# storm + kafka + hdfs

storm 0.9.6

kafka_2.10 0.8.2.2

涉及到的内容:

    kafka + spring
    storm + kafka
    storm + hdfs
    kafka + storm +hdfs

storm WordCountTopology集群环境运行方式

```bash
storm jar net.aimeizi.example.storm.WordCountTopology s1
```

# storm + kafka 集群环境搭建

拷贝kafka安装目录下lib目录下的jar到storm集群目录下的lib目录。具体拷贝清单文件如下:

```
kafka_2.10-0.8.2.2.jar
kafka-clients-0.8.2.2.jar
metrics-core-2.2.0.jar
scala-library-2.10.4.jar
snappy-java-1.1.1.7.jar
zkclient-0.3.jar
zookeeper-3.4.6.jar
storm-kafka-0.9.6.jar
guava-11.0.2.jar
curator-framework-2.5.0.jar
curator-client-2.5.0.jar
log4j-1.2.16.jar
jopt-simple-3.2.jar
```

storm kafka集群测试环境运行

```bash
storm jar net.aimeizi.example.MyKafkaTopology s1
```

# flume + kafka flume整合kafka

flume整合kafka。下面的配置为flume收集`/var/log/bootstrap.log`文件内容到kafka集群

flume-1.6.0 + kafka_2.10-0.8.2.2

flume-kafka.properties

```
#agent section
a.sources = r
a.sinks = k
a.channels = c

#source section
a.sources.r.type = exec
a.sources.r.channels = c
a.sources.r.command = tail -n 1000 /var/log/bootstrap.log

#sink section
a.sinks.k.type = org.apache.flume.sink.kafka.KafkaSink
a.sinks.k.topic = kafka-storm
a.sinks.k.brokerList = 192.168.64.128:9092,192.168.64.129:9092,192.168.64.131:9092
a.sinks.k.requiredAcks = 1
a.sinks.k.batchSize = 20
a.sinks.k.channel = c

#channel section
a.channels.c.type = memory
a.channels.c.capacity = 1000
a.channels.c.transactionCapacity = 100

# Bind the source and sink to the channel
a.sources.r.channels = c
a.sinks.k.channel = c
```

开始采集

```bash
bin/flume-ng agent -n a -c conf -f conf/flume-kafka.properties
```

# 参考文章

https://github.com/spring-projects/spring-integration-kafka

https://github.com/smallnest/spring-kafka-demo

http://shiyanjun.cn/archives/934.html

http://blog.csdn.net/weijonathan/article/details/18301321