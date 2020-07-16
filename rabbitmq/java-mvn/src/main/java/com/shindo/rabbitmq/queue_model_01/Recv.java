package com.shindo.rabbitmq.queue_model_01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author: 杨耿
 * @Description:
 * @Date: Create in 2020/7/15
 * @Modified By:
 * @Modified Date:
 */
public class Recv {
    private final static String QUEUE_NAME= "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        //指定用户名创建连接，解决guest默认localhost访问，不能外网访问的问题。
        //要解决guest账号外网不能访问问题，也可参考：https://blog.csdn.net/doubleqinyan/article/details/81081673
        factory.setUsername("root");
        factory.setPassword("123456");
        factory.setHost("123.57.131.180");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println(" [*] Waiting for message. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery)->{
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println(" [x] Received '"  + message + "'");
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,comsumerTag->{});

    }

}
