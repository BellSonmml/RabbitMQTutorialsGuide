package rose.simplepattern.subscribe.direct;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class DirectReceiveLogSpace {
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        Connection consumerConnection = factory.newConnection();

        //路由键和绑定键完全匹配队列才能接收到消息
        DirectReceiveLog receiveLog0 = DirectReceiveLog.builder().connection(consumerConnection)
                .exchangeName("direct_log")
                .exchangeType("direct")
                .workName("thread[0]")
                .severityLevels(new String[]{"error","info","warn"}).build();
        DirectReceiveLog receiveLog1 = DirectReceiveLog.builder().connection(consumerConnection)
                .exchangeName("direct_log")
                .exchangeType("direct")
                .workName("thread[1]")
                .severityLevels(new String[]{"error"}).build();

        receiveLog0.run();
        receiveLog1.run();
    }
}
