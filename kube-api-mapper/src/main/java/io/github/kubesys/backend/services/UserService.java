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
	 * @param json                  json 
	 * @return                      json (kubectl get users)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "创建用户，使用Kubernetes的规范")
	public JsonNode createUser(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			JsonNode json) throws Exception {
		
		String kind = KubeUtil.getKind(json);
		
		if (!isExpected(kind, "User")) {
			throw new Exception("it is not a user.");
		}
		
		JsonNode spec = json.get("spec");
		((ObjectNode) json).remove("spec");

		
		((ObjectNode) json).set("spec", completeUser(json.get(
					"metadata").get("name").asText(), spec));
		
		return client.createResource(json);
	}

	
	/**
	 * @param json                  json 
	 * @return                      json (kubectl get users)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "更新用户信息，使用Kubernetes的规范")
	public JsonNode updateUser(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			JsonNode json) throws Exception {
		
		String kind = KubeUtil.getKind(json);
		
		if (!isExpected(kind, "User")) {
			throw new Exception("it is not a user.");
		}

		JsonNode spec = json.get("spec");
		((ObjectNode) json).remove("spec");

		((ObjectNode) json).set("spec", completeUser(json.get(
					"metadata").get("name").asText(), spec));
		
		return client.updateResource(json);
	}

	
	/**
	 * @param json                  json 
	 * @return                      json (kubectl get users)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "删除用户，使用Kubernetes的规范")
	public JsonNode deleteUser(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			JsonNode json) throws Exception {
		
		String kind = KubeUtil.getKind(json);
		
		if (!isExpected(kind, "User")) {
			throw new Exception("it is not a user.");
		}
		
		ClientUtil.deleteClientIfExist(json.get("metadata").get("name").asText() + "-"
								+ json.get("spec").get("token").asText().substring(0, 8));
		return client.deleteResource(json);
	}
	
	/**
	 * @param role                  role 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "创建用户角色，使用Kubernetes的规范")
	public JsonNode createUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"name\": \"name\", \"rules\": [{ \"apiGroups\": [\"*\"],\"resources\": [\"*\"],\"verbs\": [\"*\"]}]}")
			Role role) throws Exception {
		ObjectNode userRole = (ObjectNode) new ObjectMapper().readTree(new ObjectMapper().writeValueAsBytes(role));
		client.createResource(KubeUtil.createClusterRole(role.getName(), userRole.get("rules")));
		client.createResource(KubeUtil.createServiceAccount(role.getName()));
		client.createResource(KubeUtil.createClusterRoleBinding(role.getName()));
		return userRole;
	}



	/**
	 * @param role                  json 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "更新用户角色信息，使用Kubernetes的规范")
	public JsonNode updateUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"name\": \"name\", \"rules\": [{ \"apiGroups\": [\"*\"],\"resources\": [\"*\"],\"verbs\": [\"*\"]}]}")
			Role role) throws Exception {
		ObjectNode userRole = (ObjectNode) new ObjectMapper().readTree(new ObjectMapper().writeValueAsBytes(role));
		ObjectNode clusterRole = (ObjectNode) client.getResource("ClusterRole", "default", role.getName());
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
			Role role) throws Exception {
		ObjectNode userRole = (ObjectNode) new ObjectMapper().readTree(new ObjectMapper().writeValueAsBytes(role));
		client.deleteResource("ServiceAccount", "default", role.getName());
		client.deleteResource("ClusterRole", "default", role.getName());
		client.deleteResource("ClusterRoleBinding", "default", role.getName());
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

			JsonNode user = client.getResource("User", namespace, username);
			String base64DecodePassword = getBase64DecodePassword(getPasswordFromUser(user.get("spec")));
			if (base64DecodePassword
								.equals(password)) {
				ObjectNode node = new ObjectMapper().createObjectNode();
				node.put("token", username + "-" + user.get("spec").get("token").asText().substring(0, 8));
				return node;
			} else {
				throw new Exception("wrong password.");
			}
		} catch (Exception ex) {
			throw new Exception("cannot find valid User [" + username + "] in namespace [" + namespace + "], it may not exist or does not have token");
		}
		
    }
	
	

	/**********************************************************************
	 * 
	 *                     Utils
	 *
	 ***********************************************************************/
	
	
	/**
	 * @param value                  value
	 * @param expected               expected
	 * @return                       true or false
	 */
	protected boolean isExpected(String value, String expected)  {
		return value.equals(expected);
	}
	
	/**
	 * @param password               password
	 * @return                       base64-based password
	 */
	protected String getBase64Password(String password) {
		return StringUtil.isBase64(password) ? password :
				Base64.getEncoder().encodeToString(password.getBytes());
	}
	/**
	 * @param password               password
	 * @return                       encodePassword
	 */
	protected String getBase64DecodePassword(String password) {
		return StringUtil.isBase64(password) ?
				new String(Base64.getDecoder().decode(password)) : password;
	}
	
	/**
	 * @param spec                  spec
	 * @return                      name
	 */
	protected String getNameFromUser(JsonNode spec) {
		return spec.get("role").get("name").asText();
	}

	/**
	 * @param spec                  spec
	 * @return                      namespace
	 */
	protected String getNamespaceFromUser(JsonNode spec) {
		return spec.get("role").has("namespace") ? 
				spec.get("role").get("namespace").asText() : "default";
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
	 * @return                       user
	 * @throws Exception             exception
	 */
	protected ObjectNode completeUser(String user, JsonNode spec) throws Exception {
		ObjectNode newSpec = new ObjectMapper().createObjectNode();
		newSpec.put("password", getBase64Password(
				getPasswordFromUser(spec)));
		newSpec.set("role", spec.get("role"));
		String token = KubeUtil.getToken(client, 
				getNamespaceFromUser(spec), 
				getNameFromUser(spec));
		newSpec.put("token", token);
		newSpec.set("info", spec.get("info"));
		ClientUtil.createClientIfNotExist(user + "-" + token.substring(0, 8));
		return newSpec;
	}
	
	public static class User {
		
		protected String name;
		
		protected String password;
		
		protected String role;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
		
	}
}
