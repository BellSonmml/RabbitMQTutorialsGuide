package rose.simplepattern.rpc;

import com.rabbitmq.client.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 1.receive message from rpc queue
 * 2.processing message
 * 3.sent message to temp queue
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RPCServer implements Runnable {
    private String queueName;

    private String serverName;

    private Connection connection;

    private Channel channel;

    @Override
    public void run() {

        try {
            channel = connection.createChannel();
            channel.basicQos(1);
            channel.queueDeclare(queueName, false, false, false, null);
            log.info("{} waiting by {}", serverName, queueName);
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String input = new String(body, StandardCharsets.UTF_8);
                    int fib = fib(Integer.parseInt(input));
                    String correlationId = properties.getCorrelationId();
                    log.info("{} received message for input:{},correlationId:{},fibonacci:{}", serverName, input, correlationId, fib);
                    String replyTo = properties.getReplyTo();
                    channel.basicPublish("", replyTo, properties, String.valueOf(fib).getBytes(StandardCharsets.UTF_8));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };
            channel.basicConsume(queueName, false, consumer);
        } catch (IOException e) {
            log.error("rpc server process happen error:{}", e.getLocalizedMessage());
        }
    }

    private int fib(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }
}
