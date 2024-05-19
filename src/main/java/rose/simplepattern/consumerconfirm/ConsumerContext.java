package rose.simplepattern.consumerconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerContext {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel indivialChannel = connection.createChannel();
        indivialChannel.confirmSelect();

        Channel batchChannel = connection.createChannel();
        batchChannel.confirmSelect();

        Channel asyncChannel = connection.createChannel();
        asyncChannel.confirmSelect();

        IndividualConsumer ic = new IndividualConsumer(indivialChannel, "individualConsumer", "individualQueue");

        BatchConsumer bc = new BatchConsumer(batchChannel, "batchQueue", "batchConsumer", 100, 0);

        AsyncAckConsumer ac = new AsyncAckConsumer(asyncChannel, "asyncQueue", "asyncConsumer");

        ic.run();
        bc.run();
        ac.run();
    }
}
