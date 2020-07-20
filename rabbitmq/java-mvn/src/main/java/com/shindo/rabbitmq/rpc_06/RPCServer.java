package com.shindo.rabbitmq.rpc_06;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * RPC模式
 */
public class RPCServer {
	private static final String RPC_QUEUE_NAME = "rpc_queue";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.57.131.180");
		factory.setUsername("root");
		factory.setPassword("123456");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
		channel.queuePurge(RPC_QUEUE_NAME);
		

	}
}
