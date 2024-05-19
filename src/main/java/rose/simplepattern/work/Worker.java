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
            //构建工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置目标地址
            factory.setHost("localhost");
            //创建连接
            Connection connection = factory.newConnection();
            //构建通道
            Channel channel = connection.createChannel();
            //定义队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [" + workName + "] Waiting for message.");

            //自定义回调 DeliverCallback
            //Delivery消息包装体
            //consumerTag消息标识
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                try {
                    System.out.println(" [" + workName + "] Received '" + message + "'");
                    doWork(message);
                } catch (Exception e) {
                    System.out.println(" execute resources-intensive task happen exception");
                } finally {
                    System.out.println(" [" + workName + "] execute end " + message);
                    //默认自动确认
                    //DeliveryTag 发送标记，或者是递增的消息标识
                    //multiple 为true则表示确认 <=currentTag的所有消息
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            boolean autoAck = false;
            //绑定队列、非自动确认
            //如果是自动确认，消息到达后就会向服务器发送确认信息，即使消息还没有被消费
            channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            System.out.println(" execute resources-intensive task happen exception");
        }
    }
}
