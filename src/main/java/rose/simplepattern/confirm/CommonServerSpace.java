package rose.simplepattern.confirm;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CommonServerSpace {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        CommonServer server0 = new CommonServer("queue_confirm","server0",connection);
        CommonServer server1 = new CommonServer("queue_confirm","server1",connection);
        CommonServer server2 = new CommonServer("queue_confirm","server2",connection);
        server0.run();
        server1.run();
        server2.run();
    }
}
