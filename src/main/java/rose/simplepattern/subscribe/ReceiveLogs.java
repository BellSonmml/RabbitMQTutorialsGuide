package rose.simplepattern.subscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveLogs implements Runnable {

    private static final String EXCHANGE_NAME = "logs";

    private Connection connection;

    private String name;

    public ReceiveLogs() {
    }

    public ReceiveLogs(Connection connection,String name) {
        this.connection = connection;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Channel channel = connection.createChannel();

            //如果队列没有显式的绑定任何交换机，那么就会默认绑定一个名称为“”空字符，类型为redirect的交换机
            //binding的bindingkey默认是队列名称，所以bindingkey是“”
            //binding是交换机和队列之间的关联关系，指明了从交换机路由到队列的规则，通过routingkey和bindingkey的匹配关系决定消息路由到哪个队列。
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            System.out.println(name+" Waiting for message");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(name+" Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            System.out.println(name+" channel error:" + e.getLocalizedMessage());
        }
    }
}
