package rose.simplepattern.subscribe.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DirectReceiveLog implements Runnable {

    private Connection connection;

    private String exchangeName;

    private String exchangeType;

    private String workName;

    private String[] severityLevels;


    @Override
    public void run() {

        try {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, exchangeType);

            String queueName = channel.queueDeclare().getQueue();

            Arrays.stream(severityLevels).forEach(item -> {
                try {
                    channel.queueBind(queueName, exchangeName, item);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(workName + " Waiting for..." + item);
            });

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(workName + " Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println(workName + " channel error:" + e.getLocalizedMessage());
        }

    }
}
