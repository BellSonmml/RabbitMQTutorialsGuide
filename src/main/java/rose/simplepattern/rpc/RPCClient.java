package rose.simplepattern.rpc;

import com.rabbitmq.client.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 1.generate correlationId for each message
 * 2.publish message to rpc queue
 * 3.receive message which Server sent from temp queue
 * 4.check correlationId if exits completed this message;
 */
@Slf4j
@NoArgsConstructor
@Data
public class RPCClient {

    private String queueName;
    private Connection connection;
    private Channel channel;
    private String replyQueueName;

    private final List<String> recordList = new ArrayList<>();

    public RPCClient(Connection connection) {
        try {
            channel = connection.createChannel();

            if (!Objects.equals(replyQueueName, ""))
                replyQueueName = channel.queueDeclare().getQueue();

            //declare consumer listen replyQueue
            DefaultConsumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String input = new String(body, StandardCharsets.UTF_8);
                    String correlationId = properties.getCorrelationId();
                    log.info("received message for input:{},correlationId:{}", input, correlationId);
                    if (recordList.contains(correlationId))
                        log.info("bingo-----{}",correlationId);
                }
            };
            channel.basicConsume(replyQueueName, true, consumer);
        } catch (IOException e) {
            log.error("rpc client happen error:{}", e.getLocalizedMessage());
        }


    }

    public void call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();
        recordList.add(corrId);
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        String response = null;
        // 发送请求
        channel.basicPublish("", queueName, props, message.getBytes(StandardCharsets.UTF_8));
    }
}
