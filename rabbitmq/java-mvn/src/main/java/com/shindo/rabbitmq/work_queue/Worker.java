package com.shindo.rabbitmq.work_queue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author: 杨耿
 * @Description:
 * @Date: Create in 2020/7/16
 * @Modified By:
 * @Modified Date:
 */
public class Worker {
    private final static String TASK_QUEUE_NAME = "hello";

    public static void main(String[] args)throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("123.57.131.180");
        factory.setUsername("root");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);
        System.out.println(" [*] Waiting for message. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag,delivery)->{
            String message = new String(delivery.getBody(),"UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } catch (Exception e) {
                System.out.println(" [x] Done");
            }
        };

        boolean autoAck = true;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag->{});

    }

    private static void doWork(String task)throws Exception{
        for(char ch : task.toCharArray()){
            if(ch == '.') Thread.sleep(1000);
        }
    }
}
