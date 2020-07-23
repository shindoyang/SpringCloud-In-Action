package com.shindo.rabbitmq.publisher_confirms_07;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BooleanSupplier;

public class PublisherConfirms {
	static final int MESSAGE_COUNT = 50_000;

	static Connection createConnection() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("123.57.131.180");
		factory.setUsername("root");
		factory.setPassword("123456");
		return factory.newConnection();
	}

	public static void main(String[] args) throws Exception {
		publishMessageIndividually();
		publishMessagesInBatch();
		handlePublishConfirmsAsynchronously();
	}

	//单个消息发送
	static void publishMessageIndividually() throws Exception {
		Connection connection = createConnection();
		Channel channel = connection.createChannel();

		String queue = UUID.randomUUID().toString();
		channel.queueDeclare(queue, false, false, true, null);

		channel.confirmSelect();
		long start = System.nanoTime();//纳秒级  相比System.currentTimeMillis()的毫秒级精度更高
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			String body = String.valueOf(i);
			channel.basicPublish("", queue, null, body.getBytes());
			channel.waitForConfirmsOrDie(5_000);
		}
		long end = System.nanoTime();
		System.out.format("Published %,d messages idividually in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
	}

	//批量消息发送
	static void publishMessagesInBatch() throws Exception {
		Connection connection = createConnection();
		Channel channel = connection.createChannel();

		String queue = UUID.randomUUID().toString();
		channel.queueDeclare(queue, false, false, true, null);

		channel.confirmSelect();

		int batchSize = 100;
		int outstandingMessageCount = 0;

		long start = System.nanoTime();
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			String body = String.valueOf(i);
			channel.basicPublish("", queue, null, body.getBytes());
			outstandingMessageCount++;

			if (outstandingMessageCount == batchSize) {
				channel.waitForConfirmsOrDie(5_000);
				outstandingMessageCount = 0;
			}
		}

		if (outstandingMessageCount > 0) {
			channel.waitForConfirmsOrDie(5_0000);
		}
		long end = System.nanoTime();
		System.out.format("Published %,d messages in batch in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
	}

	//发布者异步确认
	static void handlePublishConfirmsAsynchronously() throws Exception {
		Connection connection = createConnection();
		Channel channel = connection.createChannel();

		String queue = UUID.randomUUID().toString();
		channel.queueDeclare(queue, false, false, true, null);

		channel.confirmSelect();

		ConcurrentNavigableMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

		ConfirmCallback cleanOutstandingConfirms = (sequenceNumber, multiple) -> {
			if (multiple) {
				ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(sequenceNumber, true);
				confirmed.clear();
			} else {
				outstandingConfirms.remove(sequenceNumber);
			}
		};

		channel.addConfirmListener(cleanOutstandingConfirms, (sequenceNumber, multiple) -> {
			String body = outstandingConfirms.get(sequenceNumber);
			System.err.format(
					"Message with body %s has been nack-ed, Sequence number: %d, multiple: %b%n",
					body, sequenceNumber, multiple
			);
			cleanOutstandingConfirms.handle(sequenceNumber, multiple);
		});

		long start = System.nanoTime();
		for (int i = 0; i < MESSAGE_COUNT; i++) {
			String body = String.valueOf(i);
			outstandingConfirms.put(channel.getNextPublishSeqNo(), body);
			channel.basicPublish("", queue, null, body.getBytes());
		}

		if (!waitUntil(Duration.ofSeconds(60), () -> outstandingConfirms.isEmpty())) {
			throw new IllegalStateException("All message could not be confirmed in 60 seconds");
		}

		long end = System.nanoTime();
		System.out.format("Published %,d message and handled confirms asynchronously in %,d ms%n", MESSAGE_COUNT, Duration.ofNanos(end - start).toMillis());
	}

	static boolean waitUntil(Duration timeout, BooleanSupplier condition) throws InterruptedException {
		int waited = 0;
		while (!condition.getAsBoolean() && waited < timeout.toMillis()) {
			Thread.sleep(100L);
			waited = +100;
		}
		return condition.getAsBoolean();
	}

}
