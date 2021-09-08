/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.0
 *
 */
public class MessageMapper {
	
	protected final static Logger m_logger = Logger.getLogger(MessageMapper.class.getName());

	public static String DEFAULT_USERNAME = "guest";
	
	public static String DEFAULT_PASSWORD = "guest";
	
	public static String DEFAULT_HOST     = "localhost";
	
	public static String DEFAULT_PORT     = "5672";
	
	public static String DEFAULT_QUEUE    = "event";
	
	protected final String queue;
	
	protected Channel channel;
	
	
	public MessageMapper() throws Exception {
		this(DEFAULT_QUEUE, createChannel(), true);
	}
	
	public MessageMapper(boolean deleted) throws Exception {
		this(DEFAULT_QUEUE, createChannel(), deleted);
	}
	
	public MessageMapper(String queue, Channel channel, boolean deleted) throws Exception {
		this.queue = queue;
		this.channel = channel;
		if (deleted) {
			this.channel.queueDelete(queue);
		}
		this.channel.queueDeclare(queue, true, false, false, null);
	}

	public static Channel createChannel() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(getUsername());
			factory.setPassword(getPassword());
			factory.setHost(getHost());
			factory.setPort(Integer.parseInt(getPort()));
			Connection connection = factory.newConnection();
			return connection.createChannel();
		} catch (Exception ex) {
			m_logger.severe("find no AMQP, and ignore it.");
			return null;
		}
	}
	
	
	public Channel getChannel() {
		return channel;
	}

	public static String getUsername() {
		String user = System.getenv("amqpUser");
		return user == null ? DEFAULT_USERNAME : user;
	}
	
	public static String getPassword() {
		String pwd = System.getenv("amqpPassword");
		return pwd == null ? DEFAULT_PASSWORD : pwd;
	}
	
	public static String getHost() {
		String host = System.getenv("amqpHost");
		return host == null ? DEFAULT_HOST : host;
	}
	
	public static String getPort() {
		String port = System.getenv("amqpPort");
		return port == null ? DEFAULT_PORT : port;
	}

	public void send(JsonNode json) {
		try {
			this.channel.basicPublish("", queue, null, json.toPrettyString().getBytes("UTF-8"));
		} catch (IOException e) {
			m_logger.severe("unable to send data: " + json.toPrettyString());
			m_logger.severe(e.toString());
			if (this.channel != null && !this.channel.isOpen()) {
				if (channel.getConnection().isOpen()) {
					try {
						channel.getConnection().close();
					} catch (IOException e1) {
					}
				}
				try {
					this.channel = createChannel();
				} catch  (Exception e1) {
					m_logger.severe("fail to create a channel: " + e1.toString());
				}
			}
		}
	}
}
