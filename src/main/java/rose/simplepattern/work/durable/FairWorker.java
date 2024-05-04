package rose.simplepattern.work.durable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class FairWorker implements Runnable {

    public FairWorker() {
    }

    public FairWorker(String workName) {
        this.workName = workName;
    }

    private String workName;
    private final static String QUEUE_NAME = "durable";

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
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println(" [" + workName + "] Waiting for message.");

            //正在处理任务的工人不会收到新的任务
            channel.basicQos(1);

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
