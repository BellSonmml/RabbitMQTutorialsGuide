package rose.simplepattern.confirm;

import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class BatchClient {
    private Channel channel;

    private String queueName;

    private String exchangeName;

    private int count;

    private int batchSize;


    public void publishMessage(String message) throws IOException, InterruptedException, TimeoutException {
        if (count >= batchSize) {
            log.info("research {} begin waiting confirm",count);
            channel.waitForConfirmsOrDie(5000);
            count = 0;
        }
        channel.basicPublish(exchangeName, queueName, null, message.getBytes(StandardCharsets.UTF_8));
        channel.waitForConfirmsOrDie(5000);
        count++;
    }
}
