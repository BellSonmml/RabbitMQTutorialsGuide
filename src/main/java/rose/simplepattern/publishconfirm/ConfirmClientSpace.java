package rose.simplepattern.publishconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConfirmClientSpace {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();

        long individualMillis = System.currentTimeMillis();
        //开启发布确认
        Channel channel = connection.createChannel();
        channel.confirmSelect();
        IndividualClient individualClient = new IndividualClient(channel, "queue_confirm", "");
        for (int i = 0; i < 5000; i++) {
            individualClient.publishMessage("individual");
        }

        long batchMillis = System.currentTimeMillis();
        System.out.println("individual spend :"+(batchMillis-individualMillis));

        Channel batchChannel = connection.createChannel();
        batchChannel.confirmSelect();
        BatchClient batchClient = new BatchClient(batchChannel,"queue_confirm","",0,100);
        for (int i = 0; i <5000; i++) {
            batchClient.publishMessage("batch");
        }

        long asyncMillis = System.currentTimeMillis();
        System.out.println("batch spend :" +(asyncMillis - batchMillis));

        Channel asyncChannel = connection.createChannel();
        asyncChannel.confirmSelect();
        HandingAsyncClient handingAsyncClient = new HandingAsyncClient(asyncChannel, "queue_confirm", "");
        for (int i = 0; i < 5000; i++) {
            handingAsyncClient.publishMessage("async");
        }

        System.out.println("async spend :" +(System.currentTimeMillis() - batchMillis));
    }
}
