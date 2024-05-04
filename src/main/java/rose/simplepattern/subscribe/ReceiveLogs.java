package rose.simplepattern.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveLogs implements Runnable {

    private static final String EXCHANGE_NAME = "logs";

    private Connection connection;

    private String name;

    public ReceiveLogs() {
    }

    public ReceiveLogs(Connection connection,String name) {
        this.connection = connection;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            System.out.println(name+" Waiting for message");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(name+" Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println(name+" channel error:" + e.getLocalizedMessage());
        }
    }
}
