/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.backend.clients.PostgresPoolClient;
import io.github.kubesys.backend.clients.PostgresPoolClient.SQLObject;
import io.github.kubesys.backend.models.auth.User;
import io.github.kubesys.devfrk.spring.cores.AbstractHttpHandler;
import io.github.kubesys.devfrk.tools.annotations.ServiceDefinition;

@ServiceDefinition
public class SystemService extends AbstractHttpHandler {

	/**
	 * 数据库客户端
	 */
	@Autowired
	protected PostgresPoolClient postgresClient;
	
	public String login(JsonNode data) throws Exception {
		JsonNode result = postgresClient.getObject(User.class.getName(), new SQLObject(true, data));
		return result.toPrettyString();
	}
	
	public void loginout() {
		
	}
	
}
