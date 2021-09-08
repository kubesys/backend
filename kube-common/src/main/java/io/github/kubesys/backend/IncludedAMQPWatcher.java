/*

 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.kubeclient.KubernetesClient;
import io.github.kubesys.kubeclient.KubernetesWatcher;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.1
 *
 */
public abstract class IncludedAMQPWatcher extends KubernetesWatcher {

	protected final static Logger m_logger = Logger.getLogger(IncludedAMQPWatcher.class.getName());
	
	protected final MessageMapper msgClient;
	
	public IncludedAMQPWatcher(KubernetesClient client, MessageMapper msgClient) {
		super(client);
		this.msgClient = msgClient;
	}

	@Override
	public void doAdded(JsonNode node) {
		ObjectNode json = new ObjectMapper().createObjectNode();
		json.put("type", "ADDED");
		json.set("data", node);
		if (this.msgClient != null) {
			this.msgClient.send(json);
		} else {
			m_logger.severe("find no AMQP, and ignore it.");
		}
	}

	@Override
	public void doModified(JsonNode node) {
		ObjectNode json = new ObjectMapper().createObjectNode();
		json.put("type", "UPDATED");
		json.set("data", node);
		if (this.msgClient != null) {
			this.msgClient.send(json);
		} else {
			m_logger.severe("find no AMQP, and ignore it.");
		}
	}

	@Override
	public void doDeleted(JsonNode node) {
		ObjectNode json = new ObjectMapper().createObjectNode();
		json.put("type", "DELETED");
		json.set("data", node);
		if (this.msgClient != null) {
			this.msgClient.send(json);
		} else {
			m_logger.severe("find no AMQP, and ignore it.");
		}
	}
}
