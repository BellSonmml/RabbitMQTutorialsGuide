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
            //定义通道的持久化，通道定义的matedata持久化到磁盘中，即使服务器重启，通道也不会丢失，预防发生重启后发生通道/队列不存在的情况
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println(" [" + workName + "] Waiting for message.");

            //正在处理任务的工人不会收到新的任务
            //AMQP协议消息是通过通道传输的，所以公平机制是针对通道的
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
            //多个消费者共用同一个通道，多个消费者竞争一个通道的消息，可能导致通道负载过重，影响消息传输效率
            //多个消费者由于竞争，引发并发控制问题。类似于消息的确认时机，多个消费者的并发问题。
            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            System.out.println(" execute resources-intensive task happen exception");
        }
    }
}
