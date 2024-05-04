package rose.simplepattern.rpc;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RPCClientSpace {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        RPCClient rpcClient = new RPCClient(connection);
        rpcClient.setQueueName("queue_rpc");
        rpcClient.call("0");
        rpcClient.call("1");
        rpcClient.call("2");
    }
}
