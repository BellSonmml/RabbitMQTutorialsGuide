### RabbitMQ官网教学(完整版)

### RabbitMQ的作用：削峰填谷，异步解耦

RabbitMQ作用概括：应用接口，异步处理，流量削峰，消息通讯，远程调用

#### RabbitMQ为什么能解决数据量高发服务处理繁忙的问题
答：服务器load高时 可以暂时做消息积压 或者可以丢弃消息

### RabbitMQ基本概念
生产者：产生数据
</br>交换机：决定消息如何处理【1.发送指定队列 2.发送多个队列 3.丢弃消息】
</br>队列：消息缓冲区
</br>消费者：处理数据

### 队列七大模型
##### 1.Helloworld
简单收发没有持久化 没有消息队列共享。

##### 2.WorkQueues
队列共享：已有任务的消费者不再接受消息(在消费者中通过channel.basicQos(1))来开启
</br>可持久化：生产者绑定队列时，生产者发布消息时声明消息和队列都是持久的。

##### 3.Publish/Subscribe
通过申明指定类型的exchange发布消息路由到队列上共享消费，消费者通道绑定exchange和队列

##### 4.routing模式
绑定redirect交换机后,生产者发布消息携带路由键到交换机。
</br>消费者定义redirect交换机，将队列通过bind与交换机、路由键绑定
</br>单个维度binding，例如级别或者类型。

##### 5.topic
绑定topic交换机后,生产者发布消息携带路由键到交换机。
</br>消费者定义topic交换机，将队列通过bind与交换机、路由键绑定
</br>支持多个维度binging。路由键上限255个字节为限，多个维度之间以.隔开。

##### 6.rpc
远程调用，客户端调用之后需要阻塞等待服务端的响应。
</br>远程调用需要做一下几点：
  </br>1.消息与标识的对应，消息携带回调队列信息
  </br>2.消费者处理之后，回传消息到回调队列【消费者只ack单个消息】
  </br>3.生产者接受来自回调队列的消息，检查消息与标识是否对应，确认之后通知回调队列

##### 7.publishconfirms
发布确认主要有以下三个类型：
  </br>1.individual 单个消息确认
  </br>2.batch 批量确认
  </br>3.Asynchronously异步确认
</br>发布确认是通道级别的操作，使用api channel.confirmSelect()开启，通过在通道上注册监听者来做两件事情
  </br>1:ack消息的确认【包括清理标识和消息之间的对应关系】
  </br>2:nack消息的重发【包括记录异常日志等】
  
### 为了便于理解添加图示
</br>
<img width="647" alt="image" src="https://github.com/BellSonmml/RabbitMQTutorialsGuide/assets/80883227/c1dca056-f582-4d03-a402-06ce46dc6ecd">

#### 补充知识点
学习笔记请参考<a href="https://github.com/BellSonmml/RabbitMQTutorialsGuide/