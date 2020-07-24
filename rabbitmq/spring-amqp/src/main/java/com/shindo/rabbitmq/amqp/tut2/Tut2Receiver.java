package com.shindo.rabbitmq.amqp.tut2;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

@RabbitListener(queues = "tut.queue")
public class Tut2Receiver {
	private final int instance;

	public Tut2Receiver(int i) {
		this.instance = i;
	}

	//StopWatch是org.springframework.util包下的工具类，可方便的对程序部分代码进行计时(ms级别)，适用于同步单线程代码块。
	@RabbitHandler
	public void receive(String in) throws InterruptedException {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		System.out.println("instance " + this.instance + " [X] Received '" + in + "'");
		doWork(in);
		stopWatch.stop();
		System.out.println("instance " + this.instance + " [X] Done in " + stopWatch.getTotalTimeSeconds() + "s");
	}

	private void doWork(String in) throws InterruptedException {
		for (char ch : in.toCharArray()) {
			if (ch == '.') {
				Thread.sleep(1000);
			}
		}
	}

}
