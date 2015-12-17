package net.aimeizi.example.kafka.spring;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring kafka 消息消费者
 * Created by fengjing on 2015/12/15.
 */
public class Consumer {
    private static final String CONFIG = "/consumerContext.xml";
    private static Random rand = new Random();

    @SuppressWarnings({"unchecked", "unchecked", "rawtypes"})
    public static void main(String[] args) {
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.toLevel("ERROR"));
        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(CONFIG, Consumer.class);
        ctx.start();
        final QueueChannel channel = ctx.getBean("inputFromKafka", QueueChannel.class);
        Message msg;
        while ((msg = channel.receive()) != null) {
            HashMap map = (HashMap) msg.getPayload();
            Set<Map.Entry> set = map.entrySet();
            for (Map.Entry entry : set) {
                String topic = (String) entry.getKey();
                System.out.println("Topic:" + topic);
                ConcurrentHashMap<Integer, List<byte[]>> messages = (ConcurrentHashMap<Integer, List<byte[]>>) entry.getValue();
                Collection<List<byte[]>> values = messages.values();
                for (Iterator<List<byte[]>> iterator = values.iterator(); iterator.hasNext(); ) {
                    List<byte[]> list = iterator.next();
                    for (byte[] object : list) {
                        String message = new String(object);
                        System.out.println("Message: " + message);
                    }
                }
            }
        }
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ctx.close();
    }
}
