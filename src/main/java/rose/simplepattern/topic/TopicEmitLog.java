package rose.simplepattern.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopicEmitLog {
    private String exchangeName;

    private String exchangeType;

    private Connection connection;
    public void publishTwoElementMessage(String routingKey,String message)
    {
        try {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName,exchangeType);

            channel.basicPublish(exchangeName,routingKey,null,message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        System.out.println("[x] sent '"+routingKey+"':'"+message+"'");
    }
}
