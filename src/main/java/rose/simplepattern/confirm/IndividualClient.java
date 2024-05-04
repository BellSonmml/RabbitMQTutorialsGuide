package rose.simplepattern.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Slf4j
public class IndividualClient {

    private Channel channel;

    private String queueName;

    private String exchangeName;


    public void publishMessage(String message) throws IOException, InterruptedException, TimeoutException {
        channel.basicPublish(exchangeName,queueName,null,message.getBytes(StandardCharsets.UTF_8));
        channel.waitForConfirmsOrDie(5000);
    }
}
