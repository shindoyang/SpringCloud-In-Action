package com.shindo.rabbitmq.amqp.tut1;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"tut1", "hello-world"})
@Configuration
public class Tut1Config {

	@Bean
	public Queue hello() {
		return new Queue("hello2");
	}

	@Profile("receiver")
	@Bean
	public Tut1Receive receive() {
		return new Tut1Receive();
	}

	@Profile("sender")
	@Bean
	public Tut1Sender sender() {
		return new Tut1Sender();
	}

}
