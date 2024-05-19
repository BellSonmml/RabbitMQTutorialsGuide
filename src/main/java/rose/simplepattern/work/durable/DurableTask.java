package rose.simplepattern.work.durable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;

public class DurableTask {
    private final static String QUEUE_NAME = "durable";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            StringBuilder message = new StringBuilder("Hello World!");
            for (int i = 0; i < 10; i++) {
                message.append(".");
                channel.basicPublish(""
                        , QUEUE_NAME
                        //deliveryMode=2将消息设置为持久化
                        , MessageProperties.PERSISTENT_TEXT_PLAIN
                        , message.toString().getBytes(StandardCharsets.UTF_8));
                Thread.sleep(1000);
                System.out.println(" [" + i + "] Sent '" + message + "'");
            }
        }
    }
}
