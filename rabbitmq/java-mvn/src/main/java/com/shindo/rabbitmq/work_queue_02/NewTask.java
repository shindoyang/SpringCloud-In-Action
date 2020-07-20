package com.shindo.rabbitmq.work_queue_02;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 竞争消费模式 competing consumer pattern
 * <p>
 * RabbitMQ server会以轮询的机制，将消息均匀地发送到各个client， 即便某个client的处理能力强，很快回复了Basic.ACK，仍如还是按round-robin的方式，依次发送，
 * 因为server并不是等待Basic.ACK后，才发生，而是收到message后，就根据round-robin的轮询方式发送， 在收到Basic.ACK后，将消息从队列中删除。如果我们很明确知道某个client的处理能力很强，
 * 可以利用 channel.basicQos(2); ，每次分发两个消息给它，相当于weight=2。
 */
public class NewTask {
	private final static String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.57.131.180");
		factory.setUsername("root");
		factory.setPassword("123456");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//消息持久化
		boolean durable = true;
		channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        /*
        channel.queueDeclare(队列名称，是否持久化，独占的queue，不使用时是否自动删除，其他参数)
         */

		String message = "netWork 16 message..";
		//消息持久化改造:basicProperties = MessageProperties.PERSISTENT_TEXT_PLAIN
		channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Send " + message + "'");
	}
}
