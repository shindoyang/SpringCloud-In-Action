package com.shindo.rabbitmq.work_queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: 杨耿
 * @Description:
 * @Date: Create in 2020/7/16
 * @Modified By:
 * @Modified Date:
 */
public class NewTask {
    private final static String QUEUE_NAME="hello";

    public static void main(String[] args)throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("123.57.131.180");
        factory.setUsername("root");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "netWork 16 message..";
        channel.basicPublish("", "hello",null, message.getBytes());
        System.out.println(" [x] Send " + message + "'");
    }
}
