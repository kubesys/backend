/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import org.springframework.beans.factory.annotation.Autowired;


import io.github.kubesys.backend.clients.PostgresPoolClient;
import io.github.kubesys.devfrk.spring.cores.AbstractHttpHandler;
import io.github.kubesys.devfrk.tools.annotations.ServiceDefinition;

@ServiceDefinition
public class SystemService extends AbstractHttpHandler {

	
	/**
	 * 数据库客户端
	 */
	@Autowired
	protected PostgresPoolClient postgresClient;
	
	public String login(String role, String username, String password) throws Exception {
		
//		throw new WrongUserOrPasswordException(ExceptionConstants.WRONG_USERNAME_OR_PASSWORD);
		return null;
	}
	
	
	public void loginout() {
		
	}
	
}
