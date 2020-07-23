package com.shindo.rabbitmq.amqp.tut1;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "hello2")
public class Tut1Receive {

	@RabbitHandler
	public void receive(String in) {
		System.out.println(" [X] Received '" + in + "'");
	}
}
