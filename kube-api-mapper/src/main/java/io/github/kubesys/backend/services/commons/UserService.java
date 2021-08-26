/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services.commons;

import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.httpfrk.core.HttpBodyHandler;
import com.github.kubesys.tools.annotations.ServiceDefinition;

import io.github.kubesys.backend.utils.ClientUtil;
import io.github.kubesys.backend.utils.KubeUtil;
import io.github.kubesys.backend.utils.StringUtil;
import io.github.kubesys.kubeclient.KubernetesClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * @author lichengzhi99@otcaix.iscas.ac.cn
 * @author wuheng@otcaix.iscas.ac.cn
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
				res.put("role", spec.get("role").get("name").asText());
				return res;
			}catch (Exception e) {
				throw new Exception("can not get user resource or do not have specific field");
			}
	}
	
	/**
	 * @param json                  json 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "创建用户角色，使用Kubernetes的规范")
	public JsonNode createUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			JsonNode json) throws Exception {

		String kind = KubeUtil.getKind(json);

		if (!isExpected(kind, "UserRole")) {
			throw new Exception("it is not a userrole.");
		}

		String namespace = KubeUtil.getNamespace(json);
		String name = KubeUtil.getName(json);

		client.createResource(KubeUtil
				.createServiceAccount(namespace, name));
		client.createResource(KubeUtil
				.createClusterRole(name, json.get("rules")));
		client.createResource(KubeUtil
				.createClusterRoleBinding(namespace, name));
		
		return client.createResource(json);
	}



	/**
	 * @param json                  json 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "更新用户角色信息，使用Kubernetes的规范")
	public JsonNode updateUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			JsonNode json) throws Exception {
		String kind = KubeUtil.getKind(json);
		
		if (!isExpected(kind, "UserRole")) {
			throw new Exception("it is not a userrole.");
		}
		
		String name = KubeUtil.getName(json);
		
		client.updateResource(KubeUtil
				.createClusterRole(name, json.get("rules")));
		
		return client.updateResource(json);
	}
	
	/**
	 * @param json                  json 
	 * @return                      json (kubectl get userroles)
	 * @throws Exception            exception
	 */
	@ApiOperation(value = "删除用户角色，使用Kubernetes的规范")
	public JsonNode deleteUserRole(
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}")
			JsonNode json) throws Exception {
		String kind = KubeUtil.getKind(json);
		
		if (!isExpected(kind, "UserRole")) {
			throw new Exception("it is not a userrole.");
		}
		
		String namespace = json.get("metadata").get("namespace").asText();
		String name = json.get("metadata").get("name").asText();
		
		client.deleteResource("ServiceAccount", namespace, name);
		client.deleteResource("ClusterRole", name);
		client.deleteResource("ClusterRoleBinding", name);
		
		return client.deleteResource(json);
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
}
