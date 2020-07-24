package com.shindo.rabbitmq.topics_05;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 主题模式
 * 是路由模式的升级，配置路由键可以使用 * # 进行模糊匹配，其中# 可以代表零个或多个单词，* 仅代表一个单词
 */
public class EmitLogTopic {
	private static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.57.131.180");
		factory.setUsername("root");
		factory.setPassword("123456");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "topic");

		String routingKey = getRouting(args);
		String message = getMessage(args);

		channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
	}

	private static String getRouting(String[] args) {
		if (args.length < 1)
			return "anonymous.info";
		return args[0];
	}

	private static String getMessage(String[] args) {
		if (args.length < 2)
			return "Hello World!";
		return joinStrings(args, " ", 1);
	}

	private static String joinStrings(String[] args, String delimiter, int startIndex) {
		for (String temp : args) {
			System.out.println("=========== " + temp);
		}
		int length = args.length;
		if (length == 0) return "";
		if (length < startIndex) return "";
		StringBuilder words = new StringBuilder(args[startIndex]);
		for (int i = startIndex + 1; i < length; i++) {
			words.append(delimiter).append(args[i]);
		}
		return words.toString();
	}
}
