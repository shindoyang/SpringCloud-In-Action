package com.shindo.rabbitmq.routing_04;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 在发布/订阅模式上在进一步，队列只接受某发布的某类型消息。routing.png图以log exchange为例，不同的队列接收不同的log级别。这就是路由模式。 我们将重点学习
 * exchange模式为direct时，队列不仅与exchange关联，还与具体的routing_key绑定。
 */
public class EmitLogDirect {
	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.57.131.180");
		factory.setUsername("root");
		factory.setPassword("123456");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

		String severity = getSeverity(args);
		String message = getMessage(args);

		channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
	}

	private static String getSeverity(String[] args) {
		if (args.length < 1)
			return "info";
		return args[0];
	}

	private static String getMessage(String[] args) {
		if (args.length < 2)
			return "Hello World!";
		return joinString(args, " ", 1);
	}

	private static String joinString(String[] args, String delimiter, int startIndex) {
		int length = args.length;
		if (length == 0) return "";
		if (length <= startIndex) return "";
		StringBuffer sb = new StringBuffer();
		StringBuilder words = new StringBuilder(args[startIndex]);
		for (int i = startIndex + 1; i < length; i++) {
			words.append(delimiter).append(args[i]);
		}
		return words.toString();
	}

}
