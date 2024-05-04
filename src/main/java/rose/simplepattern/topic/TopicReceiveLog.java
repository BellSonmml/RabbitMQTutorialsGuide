package rose.simplepattern.topic;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TopicReceiveLog implements Runnable {
    private String exchangeName;
    private String exchangeType;
    private Connection connection;
    private String[] routingKey;

    private String name;

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, exchangeType);

            String queueName = channel.queueDeclare().getQueue();

            for (String item : routingKey) {
                channel.queueBind(queueName, exchangeName, item);
                System.out.println(name + " Waiting for message binding routingKey is:" + item);
            }

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                String routedKey = delivery.getEnvelope().getRoutingKey();
                System.out.println(name + " Received:" + message + ",from:" + routedKey);
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
