package rose.simplepattern.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Worker implements Runnable {

    public Worker() {
    }

    public Worker(String workName) {
        this.workName = workName;
    }

    private String workName;
    private final static String QUEUE_NAME = "hello";

    private void doWork(String message) throws InterruptedException {
        for (char ch : message.toCharArray()) {
            if (ch == '.') {
                System.out.println(workName + " executing resources-intensive task");
                Thread.sleep(1000);
            }
        }
    }

    @Override
    public void run() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [" + workName + "] Waiting for message.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                try {
                    System.out.println(" [" + workName + "] Received '" + message + "'");
                    doWork(message);
                } catch (Exception e) {
                    System.out.println(" execute resources-intensive task happen exception");
                } finally {
                    System.out.println(" [" + workName + "] execute end " + message);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            System.out.println(" execute resources-intensive task happen exception");
        }
    }
}
