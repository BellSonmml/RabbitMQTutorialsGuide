package rose.simplepattern.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            StringBuilder message = new StringBuilder("Hello World!");
            for (int i = 0; i < 10; i++) {
                message.append(".");

                channel.basicPublish(EXCHANGE_NAME, "", null, message.toString().getBytes(StandardCharsets.UTF_8));

                Thread.sleep(1000);
                System.out.println(" [" + i + "] Sent '" + message + "'");
            }
        }
    }

}
