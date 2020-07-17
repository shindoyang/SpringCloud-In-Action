package com.shindo.rabbitmq.publishsubscribe_03;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author: 杨耿 @Description: @Date: Create in 2020/7/16 @Modified By: @Modified Date:
 */
public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.57.131.180");
		factory.setUsername("root");
		factory.setPassword("123456");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [x] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback =
				(consumerTag, delivery) -> {
					String message = new String(delivery.getBody(), "UTF-8");
					System.out.println(" [x] Received '" + message + "'");
				};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
	}
}
