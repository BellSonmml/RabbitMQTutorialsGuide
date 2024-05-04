package rose.simplepattern.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SimpleTask {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            StringBuilder message = new StringBuilder("Hello World!");
            for (int i = 0; i < 10; i++) {
                message.append(".");
                channel.basicPublish("", QUEUE_NAME, null, message.toString().getBytes());
                Thread.sleep(1000);
                System.out.println(" [" + i + "] Sent '" + message + "'");
            }
        }
    }
}
