package rose.simplepattern.publishconfirm;

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
        //添加确认监听器
        //无论是在一下三种
        //1、单个消息
        //2、批量消息
        //3、消息异步
        //模式中，都可以注册通道确认监听器 对 成功确认、失败确认的消息进行逻辑处理
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                //确认成功后续逻辑
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                //确认失败后续逻辑
            }
        });
        channel.basicPublish(exchangeName,queueName,null,message.getBytes(StandardCharsets.UTF_8));
        //确认超时时间
        channel.waitForConfirmsOrDie(5000);
    }
}
