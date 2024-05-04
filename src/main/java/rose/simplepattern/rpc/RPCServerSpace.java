package rose.simplepattern.rpc;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RPCServerSpace {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        RPCServer server0 = RPCServer.builder()
                .serverName("server0")
                .queueName("queue_rpc")
                .connection(connection)
                .build();
        RPCServer server1 = RPCServer.builder()
                .serverName("server1")
                .queueName("queue_rpc")
                .connection(connection)
                .build();
        RPCServer server2 = RPCServer.builder()
                .serverName("server2")
                .queueName("queue_rpc")
                .connection(connection)
                .build();

        server0.run();
        server1.run();
        server2.run();
    }
}
