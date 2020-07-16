package com.shindo.rabbitmq.publishSubscribe_03;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 发布订阅者模式   Emit [发射、发出] *
 * RabbitMQ消息传递模型的核心思想是生产者从不将任何消息直接发送到队列。实际上，生产者经常甚至根本不知道是否将消息传递到任何队列。
 *
 * 事实上，producer并没有将消息写到队列，而是发送到Exchange，
 * 由exchange决定将消息放入某个队列，某些队列，亦或者丢弃这个消息。
 * exchange有几种模式： direct, topic, headers 和 fanout 。
 * publish/subcribe模式使用的是fanout，即广播到绑定在该exchange的所有队列。
 */
public class EmitLog {
    private static final String EXCHANGE_NAME= "logs";

    public static void main(String[] args)throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("123.57.131.180");
        factory.setUsername("root");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        String message = args.length <1 ?"info: Hello World!":String.join(" ", args);

        channel.basicPublish(EXCHANGE_NAME,"",null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message +"'");
    }
}
