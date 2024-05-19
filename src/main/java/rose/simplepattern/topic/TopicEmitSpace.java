package rose.simplepattern.topic;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TopicEmitSpace {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        //路由键和绑定键模式匹配队列就可以接收到消息
        TopicEmitLog topicEmitLog0 = new TopicEmitLog("topic_log", "topic", connection);
        TopicEmitLog topicEmitLog1 = new TopicEmitLog("topic_log", "topic", connection);
        topicEmitLog0.publishTwoElementMessage("kern.critical","message1");
        topicEmitLog1.publishTwoElementMessage("kern.critical","message2");
        topicEmitLog1.publishTwoElementMessage("kern.critical1","message3");
        topicEmitLog1.publishTwoElementMessage("kern1.critical","message4");
        connection.close();
    }
}
