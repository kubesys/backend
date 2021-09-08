/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;


import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2.0.0
 * 
 **/
public class AMQPTest {
	
	
	/*****************************************************************************************
	 * 
	 * Main
	 * 
	 *****************************************************************************************/

	/**
	 * @param args                               args
	 * @throws Exception                         exception
	 */
	public static void main(String[] args) throws Exception {
		
		MessageMapper msgMapper = new MessageMapper(false);
		Consumer consumer = new DefaultConsumer(msgMapper.getChannel()) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body, "utf-8");
				System.out.println(envelope.getRoutingKey() + ": " + msg);
			}
			
		};
		msgMapper.getChannel().basicConsume("event", consumer);
	}
	
}
