package rose.simplepattern.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HandingAsyncClient {
    private Channel channel;

    private String queueName;

    private String exchangeName;


    public void publishMessage(String message) throws IOException, InterruptedException, TimeoutException {
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
            }
        });
        channel.basicPublish(exchangeName,queueName,null,message.getBytes(StandardCharsets.UTF_8));
        channel.waitForConfirmsOrDie(5000);
    }
}
