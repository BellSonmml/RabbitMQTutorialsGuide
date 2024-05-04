package rose.simplepattern.topic;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicReceiveSpace {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        String exchangeName = "topic_log";
        String exchangeType = "topic";
        TopicReceiveLog work0 = TopicReceiveLog.builder()
                .name("work0")
                .exchangeName(exchangeName)
                .exchangeType(exchangeType)
                .connection(connection)
                .routingKey(new String[]{"#"}).build();

        TopicReceiveLog work1 = TopicReceiveLog.builder()
                .name("work1")
                .exchangeName(exchangeName)
                .exchangeType(exchangeType)
                .connection(connection)
                .routingKey(new String[]{"kern.*"}).build();

        TopicReceiveLog work2 = TopicReceiveLog.builder()
                .name("work2")
                .exchangeName(exchangeName)
                .exchangeType(exchangeType)
                .connection(connection)
                .routingKey(new String[]{"*.critical"}).build();

        TopicReceiveLog work3 = TopicReceiveLog.builder()
                .name("work3")
                .exchangeName(exchangeName)
                .exchangeType(exchangeType)
                .connection(connection)
                .routingKey(new String[]{"kern.*","*.critical"}).build();

        work0.run();
        work1.run();
        work2.run();
        work3.run();
    }
}
