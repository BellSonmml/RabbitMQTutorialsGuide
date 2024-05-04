package rose.simplepattern.subscribe;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class LogSpace {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();

        String name = "ReceiveLog";
        for (int i = 0; i < 3; i++) {
            ReceiveLogs receiveLogs = new ReceiveLogs(connection, name + "[" + i + "]");
            receiveLogs.run();;
        }
    }
}
