package rose.simplepattern.consumerconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerContext {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        CommonProducer producer = new CommonProducer(channel);

        for (int i = 0; i < 5000; i++) {
            producer.publishMessage("individualQueue","单个消息确认");
        }

        for (int i = 0; i < 5000; i++) {
            producer.publishMessage("batchQueue","批量消息确认");
        }

        for (int i = 0; i < 5000; i++) {
            producer.publishMessage("asyncQueue","异步消息确认");
        }
    }
}
