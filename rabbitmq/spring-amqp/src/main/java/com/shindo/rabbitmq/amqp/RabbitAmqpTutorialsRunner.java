package com.shindo.rabbitmq.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author: 杨耿
 * @Description:
 * @Date: Create in 2020/7/23
 * @Modified By:
 * @Modified Date:
 */
public class RabbitAmqpTutorialsRunner implements CommandLineRunner {
	@Value("${tutorial.client.duration:0}")
	private int duration;

	@Autowired
	private ConfigurableApplicationContext ctx;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Ready ... runnint for " + duration + "ms");
		Thread.sleep(duration);
		ctx.close();
	}
}
