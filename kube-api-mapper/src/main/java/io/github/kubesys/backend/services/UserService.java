/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.httpfrk.core.HttpBodyHandler;
import com.github.kubesys.tools.annotations.ServiceDefinition;

import io.github.kubesys.backend.rbac.Role;
import io.github.kubesys.backend.rbac.User;
import io.github.kubesys.backend.utils.ClientUtil;
import io.github.kubesys.backend.utils.KubeUtil;
import io.github.kubesys.backend.utils.StringUtil;
import io.github.kubesys.kubeclient.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * @author wuheng@otcaix.iscas.ac.cn
 *
 * see https://kubernetes.io/zh/docs/concepts/configuration/secret/
 *     https://kubernetes.io/docs/reference/access-authn-authz/rbac/
 *     https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/
 * 
 */
@ServiceDefinition
@Api(value = "用户生命周期管理")
public class UserService extends HttpBodyHandler {
    
	protected KubernetesClient client = ClientUtil.getClient("default");
	
	/**
	 * @param json                  user 
	 * @return                      json (kubectl get users)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "创建用户，使用Kubernetes的规范")
	public JsonNode createUser(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			User json) throws Exception {
		
		JsonNode roles = client.listResources("ServiceAccount", "default");
		
		boolean hasRole = false;
		for (JsonNode role : roles.get("items")) {
			String name = role.get("metadata").get("name").asText();
			if (name.equals(json.getRole())) {
				hasRole = true;
				break;
			}
		}
		
		if (!hasRole) {
			throw new RuntimeException("please create role firstly.");
		}
		
		json.setPassword(Base64.getEncoder().encodeToString(json.getPassword().getBytes()));
		
		return client.createResource(json.toJson());
	}

	
	/**
	 * @param json                  json 
	 * @return                      json (kubectl get users)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "删除用户，使用Kubernetes的规范")
	public JsonNode deleteUser(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			User json) throws Exception {
		return client.deleteResource(json.toJson());
	}
	
	/**
	 * @param json                  role 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "创建用户角色，使用Kubernetes的规范")
	public JsonNode createUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"name\": \"name\", \"rules\": [{ \"apiGroups\": [\"*\"],\"resources\": [\"*\"],\"verbs\": [\"*\"]}]}")
			Role json) throws Exception {
		ObjectNode userRole = (ObjectNode) new ObjectMapper().readTree(new ObjectMapper().writeValueAsBytes(json));
		client.createResource(KubeUtil.createClusterRole(json.getName(), userRole.get("rules")));
		client.createResource(KubeUtil.createServiceAccount(json.getName()));
		client.createResource(KubeUtil.createClusterRoleBinding(json.getName()));
		
		JsonNode saJson = client.getResource("ServiceAccount", "default", json.getName());
		while (!saJson.has("secrets")) {
			saJson = client.getResource("ServiceAccount", "default", json.getName());
		}
		String secretName = saJson.get("secrets").get(0).get("name").asText();
		String token = client.getResource("Secret", "default", secretName)
								.get("data").get("token").asText();
		String fullToken = new String(Base64.getDecoder().decode(token));
		ClientUtil.register(json.getName(), fullToken);
		userRole.put("token", fullToken);
		return userRole;
	}



	/**
	 * @param json                  json 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "更新用户角色信息，使用Kubernetes的规范")
	public JsonNode updateUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"name\": \"name\", \"rules\": [{ \"apiGroups\": [\"*\"],\"resources\": [\"*\"],\"verbs\": [\"*\"]}]}")
			Role json) throws Exception {
		ObjectNode userRole = (ObjectNode) new ObjectMapper().readTree(new ObjectMapper().writeValueAsBytes(json));
		ObjectNode clusterRole = (ObjectNode) client.getResource("ClusterRole", "default", json.getName());
		clusterRole.remove("rules");
		clusterRole.set("rules", userRole.get("rules"));
		return client.updateResource(clusterRole);
	}
	
	/**
	 * @param json                  json 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "删除用户角色，使用Kubernetes的规范")
	public JsonNode deleteUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"name\": \"name\", \"rules\": [{ \"apiGroups\": [\"*\"],\"resources\": [\"*\"],\"verbs\": [\"*\"]}]}")
			Role json) throws Exception {
		ObjectNode userRole = (ObjectNode) new ObjectMapper().readTree(new ObjectMapper().writeValueAsBytes(json));
		client.deleteResource("ServiceAccount", "default", json.getName());
		client.deleteResource("ClusterRole", "default", json.getName());
		client.deleteResource("ClusterRoleBinding", "default", json.getName());
		ClientUtil.unregister(json.getName());
		return userRole;
	}

	
	/**
	 * @param username              username
	 * @param password              password
	 * @param namespace             namespace
	 * @return                      token
	 * @throws Exception            exception 
	 */
	@ApiOperation(value = "登录")
	public JsonNode login(
			@ApiParam(value = "命名空间", required = true, example = "default")
			String namespace, 
			@ApiParam(value = "用户名", required = true, example = "admin")
			String username, 
			@ApiParam(value = "密码", required = true, example = "admin")
			String password) throws Exception {
		try {

			JsonNode userSpec = client.getResource("User", namespace, username).get("spec");
			String base64DecodePassword = getBase64DecodePassword(getPasswordFromUser(userSpec));
			if (base64DecodePassword.equals(password)) {
				ObjectNode node = new ObjectMapper().createObjectNode();
				node.put("token", ClientUtil.getBearerToken(geRoleFromUser(userSpec)));
				return node;
			} else {
				throw new Exception("wrong password.");
			}
		} catch (Exception ex) {
			throw new Exception("cannot find valid User [" + username + "] in namespace [" + namespace + "], it may not exist or does not have token");
		}
		
    }
	
	/**
	 * @param username				 username
	 * @param namespace				 namespace
	 * @return                       json(role and avatar)
	 * @throws Exception			 exception
	 */
	@ApiOperation(value = "获取用户信息")
	public JsonNode getUserInfo(
			@ApiParam(value = "命名空间", required = true, example = "default")
			String namespace, 
			@ApiParam(value = "用户名", required = true, example = "admin")
			String username) throws Exception {
			try {
				JsonNode spec = client.getResource("User", namespace, username).get("spec");
				ObjectNode res = new ObjectMapper().createObjectNode();
				res.put("role", spec.get("role").asText());
				return res;
			}catch (Exception e) {
				throw new Exception("can not get user resource or do not have specific field");
			}
	}
	

	/**********************************************************************
	 * 
	 *                     Utils
	 *
	 ***********************************************************************/
	
	
	/**
	 * @param password               password
	 * @return                       encodePassword
	 */
	protected String getBase64DecodePassword(String password) {
		return StringUtil.isBase64(password) ?
				new String(Base64.getDecoder().decode(password)) : password;
	}
	
	/**
	 * @param spec                   spec
	 * @return                       password
	 */
	protected String getPasswordFromUser(JsonNode spec) {
		return spec.get("password").asText();
	}
	
	/**
	 * @param spec                   spec
	 * @return                       password
	 */
	protected String geRoleFromUser(JsonNode spec) {
		return spec.get("role").asText();
	}
	
}
