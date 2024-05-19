package rose.simplepattern.consumerconfirm;

import com.rabbitmq.client.Channel;
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
public class CommonProducer {

    private Channel channel;

    public void publishMessage(String queueName,String message) {
        log.info("生产者发送消息内容为：{}",message);
        try {
            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("",queueName,null,message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("生产者发送消息，内容为：{},发送错误======",message);
        }
    }

}
