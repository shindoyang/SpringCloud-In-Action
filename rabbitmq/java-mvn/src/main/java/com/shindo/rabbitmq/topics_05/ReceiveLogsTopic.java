package com.shindo.rabbitmq.topics_05;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsTopic {
	private static final String EXCHACGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.57.131.180");
		factory.setUsername("root");
		factory.setPassword("123456");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHACGE_NAME, "topic");
		String queueName = channel.queueDeclare().getQueue();

		if (args.length < 1) {
			System.err.println("Usage: ReceiveLogsTopic [binding_key] ...");
			System.exit(1);
		}

		for (String bindingKey : args) {
			channel.queueBind(queueName, EXCHACGE_NAME, bindingKey);
		}

		System.out.println(" [*] Waiting for message. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + delivery.getEnvelope().getRoutingKey() + "'" + message + "'");
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});

	}

}
