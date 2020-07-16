package com.shindo.rabbitmq.queue_model;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 队列模式
 */
public class Send {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        //指定用户名创建连接，解决guest默认localhost访问，不能外网访问的问题。
        //要解决guest账号外网不能访问问题，也可参考：https://blog.csdn.net/doubleqinyan/article/details/81081673
        factory.setUsername("root");
        factory.setPassword("123456");
        factory.setHost("123.57.131.180");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = "Hello World!";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
