package rose.simplepattern.publishconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonServer implements Runnable {
    private String queueName;

    private String serverName;

    private Connection connection;

    @Override
    public void run() {
        Channel channel;
        try {
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            log.info("{} waiting message",serverName);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                log.info("{} received message:{}",serverName,message);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            };
            //消费者自动确认
            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            log.error("happen error:{}",e.getLocalizedMessage());
        }
    }
}
