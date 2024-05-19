package rose.simplepattern.consumerconfirm;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rose.simplepattern.common.ThreadPool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class AsyncAckConsumer implements Runnable {
    private Channel channel;

    private String queueName;

    private String name;

    @Override
    public void run() {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);


            log.info("消费者:{},接收到消息,内容为:{},consumerTag:{},delivery:{}", name
                    , message, consumerTag, JSONObject.toJSONString(delivery));

            //异步消息确认
            ThreadPool.singleThreadExecutor.execute(() -> {
                try {

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);

                } catch (IOException e) {
                    log.info("消费者:{},接收到消息,发生错误:{}", name, e.getLocalizedMessage());
                }
            });
        };

        try {

            channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
            });
            channel.waitForConfirmsOrDie(5000);

        } catch (IOException | InterruptedException | TimeoutException e) {
            log.info("消费者:{},接收到消息,发生错误:{}", name, e.getLocalizedMessage());
        }
        log.info("消费者:{}启动成功，等待消息生产者发布消息到队列:{}",name,queueName);
    }
}
