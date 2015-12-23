package net.aimeizi.example.flume;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import java.nio.charset.Charset;

/**
 * 使用flume api发送avro数据到flume，flume采集数据最终输出到文件。flume配置文件参考src/main/resources/flume.properties
 ***********************************************************************************************************************
 * 启动flumeng
 * flume-ng agent -n a -c conf/ -f conf/flume.properties -Dflume.root.logger=DEBUG,console
 * 运行该应用观察控制台输出
 ***********************************************************************************************************************
 * 使用flume-ng avro-client
 * flume-ng avro-client -c conf/ -H localhost -p 41414 -F /etc/passwd -Dflume.root.logger=DEBUG,console
 *
 * Created by fengjing on 2015/12/23.
 */
public class MyFlumeApp {
    public static void main(String[] args) {
        MyRpcClientFacade client = new MyRpcClientFacade();
        client.init("192.168.64.128", 41414);
        String sampleData = "Hello Flume!";
        for (int i = 0; i < 1000; i++) {
            client.sendDataToFlume(sampleData);
        }
        client.cleanUp();
    }
}

class MyRpcClientFacade {
    private RpcClient client;
    private String hostname;
    private int port;

    public void init(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.client = RpcClientFactory.getDefaultInstance(hostname, port);
//         this.client = RpcClientFactory.getThriftInstance(hostname, port); thrift
    }

    public void sendDataToFlume(String data) {
        Event event = EventBuilder.withBody(data, Charset.forName("UTF-8"));
        try {
            client.append(event);
        } catch (EventDeliveryException e) {
            client.close();
            client = null;
            client = RpcClientFactory.getDefaultInstance(hostname, port);
//            client = RpcClientFactory.getThriftInstance(hostname, port); thrift
        }
    }

    public void cleanUp() {
        client.close();
    }

}