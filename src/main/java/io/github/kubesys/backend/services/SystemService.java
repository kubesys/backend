/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.devfrk.spring.constants.ExceptionConstants;
import io.github.kubesys.devfrk.spring.cores.AbstractHttpHandler;
import io.github.kubesys.devfrk.spring.exs.WrongUserOrPasswordException;
import io.github.kubesys.devfrk.tools.annotations.ServiceDefinition;
import io.github.kubesys.mirror.cores.Env;
import io.github.kubesys.mirror.cores.clients.PostgresClient;

@ServiceDefinition
public class SystemService extends AbstractHttpHandler {

	/**
	 * k8s客户端
	 */
	protected final KubernetesClient kubeClient;
	
	/**
	 * 数据库客户端
	 */
	protected final PostgresClient postgresClient;
	
	public SystemService() {
		super();
		this.kubeClient = new KubernetesClient(
				System.getenv(Env.ENV_KUBE_URL), 
				System.getenv(Env.ENV_KUBE_TOKEN));
		this.postgresClient = new PostgresClient();
	}
	
	public String login(String username, String password) throws Exception {
		JsonNode json1 = kubeClient.getResource("Secret", "kubestack", username + "-user");
		String base64Pwd = json1.get("data").get("password").asText();
		if (base64Pwd.equals(password)) {
			JsonNode json2 = kubeClient.getResource("Secret", "kubestack", username + "-user-token");
			return json2.get("data").get("token").asText();
		}
		throw new WrongUserOrPasswordException(ExceptionConstants.WRONG_USERNAME_OR_PASSWORD);
	}
	
	
	public void loginout() {
		
	}
	
	public JsonNode getMeta() {
		return kubeClient.getKindDesc();
	}
}
