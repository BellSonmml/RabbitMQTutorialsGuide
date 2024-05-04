package rose.simplepattern.subscribe.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectEmitLog {

    private String exchangeName;

    private Connection connection;

    private String exchangeType;


    public void publishSeverityLevelMessage(String severityLevel, String message) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, exchangeType);
        channel.basicPublish(exchangeName, severityLevel, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "'");
    }
}