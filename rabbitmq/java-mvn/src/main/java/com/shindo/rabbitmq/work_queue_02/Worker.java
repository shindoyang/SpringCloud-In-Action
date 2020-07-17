package com.shindo.rabbitmq.work_queue_02;

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
		System.out.println(" [*] Waiting for message. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println(" [x] Received '" + message + "'");
			try {
				doWork(message);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//开启ack，需要每次完成业务后发送ack通知broker，这样broker才会真正认为消息被消费掉
				System.out.println(" [x]业务处理完成，开始发送ack");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				System.out.println("ack发送完毕");
			}
		};

		boolean autoAck = false;
		channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
		});

	}

	private static void doWork(String task) throws Exception {
		for (char ch : task.toCharArray()) {
			if (ch == '.') {
				Thread.sleep(1000);
			}
		}
	}
}
