package com.shindo.rabbitmq.amqp.tut1;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * hello world 队列模式
 */
public class Tut1Sender {
	@Autowired
	private RabbitTemplate template;

	@Autowired
	private Queue queue;

	@Scheduled(fixedDelay = 1000, initialDelay = 500)
	public void send() {
		String message = "Hello world";
		this.template.convertAndSend(queue.getName(), message);
		System.out.println(" [X] Sent '" + message + "'");
	}
}
