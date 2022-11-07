/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.backend.KubeClient;
import io.github.kubesys.backend.utils.FrontendUtils;
import io.github.kubesys.backend.utils.StringUtil;
import io.github.kubesys.client.KubernetesConstants;
import io.github.kubesys.specs.httpfrk.cores.HttpHandler;
import io.github.kubesys.tools.annotations.ServiceDefinition;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.3
 *
 * according to Kubernetes' API specification, providing the following APIs
 * 
 * - invoking Kubernetes
 *    - CreateResource
 *    - BindResource
 *    - UpdateResource
 *    - UpdateResourceStatus
 *    - GetResource
 *    - DeleteResource
 *    - WatchResource
 *    - WatchResources
 * - invoking Database
 *    - ListResources
 *    - queryResourceCount
 *    - queryResourceValue
 * - online handling
 *    - getMetadata
 */

@ServiceDefinition
public class KubeService extends HttpHandler {

	
	/*************************************************************************
	 * 
	 * 
	 *       Invoking Kubernetes
	 * 
	 * 
	 **************************************************************************/
	
	/**
	 * see KubernetesClient.createResource
	 * 
	 * @param token                    token
	 * @param json                     json
	 * @return                         json
	 * @throws Exception               exception
	 */
	public JsonNode createResource(
			String token,
			JsonNode json)
			throws Exception {

		return KubeClient.getClient(token).createResource(json);
	}

	/**
	 * see KubernetesClient.updateResource
	 * 
	 * @param token                    token
	 * @param json                     json
	 * @return                         json
	 * @throws Exception               exception
	 */
	public JsonNode updateResource(
			String token,
			JsonNode json)
			throws Exception {
		
		return KubeClient.getClient(token).updateResource(json);
	}

	/**
	 * see KubernetesClient.createResource and KubernetesClient.updateResource
	 * 
	 * @param token                    token
	 * @param json                     json
	 * @return                         json
	 * @throws Exception               exception
	 */
	public JsonNode createOrUpdateResource(
			String token,
			JsonNode json)
			throws Exception {
		try {
			return KubeClient.getClient(token).createResource(json);
		} catch (Exception e) {
			return KubeClient.getClient(token).updateResource(json);
		}
	}

	/**
	 * see KubernetesClient.deleteResource
	 * 
	 * @param token                   token
	 * @param kind                    kind
	 * @param namespace               namespace
	 * @param name                    name
	 * @return json                   json
	 * @throws Exception              exception
	 */
	public JsonNode deleteResource(
			String token,
			String kind,
			String namespace,
			String name) 
			throws Exception {

		return KubeClient.getClient(token).deleteResource(kind, namespace, name);
	}

	/**
	 * see KubernetesClient.getResource
	 * 
	 * @param token                   token
	 * @param kind                    kind
	 * @param namespace               namespace
	 * @param name                    name
	 * @return json                   json
	 * @throws Exception              exception
	 */
	public JsonNode getResource(
			String token,
			String kind,
			String namespace,
			String name) throws Exception {
		
		try {
			return KubeClient.getClient(token).getResource(kind, namespace, name.toLowerCase());
		} catch (Exception ex) {
			if ((kind.equals("Template") || kind.equals("doslab.io.Template")) && name.endsWith("create")) {
				return getTemplate(token, name); 
			}
			
			if (kind.equals("Frontend") || kind.equals("doslab.io.Frontend")) {
				int idx = name.indexOf("-");
				if (idx != -1) {
					String key = name.substring(0, idx);
					String json = FrontendUtils.getJson(key, name);
					if (json != null) {
						JsonNode result = new ObjectMapper().readTree(json);
						KubeClient.getClient(token).createResource(result);
						FrontendUtils.writeAsYaml(name, result);
						return result;
					}
				}
			}
			
			throw new Exception("获取创建资源失败, 命名空间" + namespace + "类型为" + kind + "的资源" + name +"不存在. ");
		}
	}

	
	/*************************************************************************
	 * 
	 * 
	 *       Invoking Database
	 * 
	 * 
	 **************************************************************************/
	
	/**
	 * equal to see KubernetesClient.listResources
	 * 
	 * @param token                   token
	 * @param kind                    kind
	 * @param namespace               namespace
	 * @return json                   json
	 * @throws Exception              exception
	 */
	public JsonNode listResources(
			String token,
			String kind,
			int limit,
			int page,
			Map<String, String> labels)
			throws Exception {

			try {
				// check permissions
				KubeClient.getClient(token).listResources(kind, KubernetesConstants.VALUE_ALL_NAMESPACES, null, null, 1, null);
			} catch (Exception ex) {
				// sql connection may timeout, retry it
				throw new Exception("没有权限, 无法操作资源类型 " + kind + ". ");
			}
			
			return KubeClient.sqlMapper().query(getTable(token, getFullKind(token, kind)), 
											getKind(kind), 
											limit, page, 
											(labels != null) ? labels : new HashMap<>());
	}

	
	/**
	 * how many items.
	 * 
	 * @param token                    token
	 * @param data                     data
	 * @return                         json
	 * @throws Exception               exception
	 */
	public JsonNode queryResourceCount (
			String token,
			JsonNode data)
			throws Exception {
			
		String fullKind = getFullKind(token, data.get("link").asText());
		return KubeClient.sqlMapper().queryCount(getTable(token, fullKind),
						data.get("tag").asText(), data.get("value").asText());
	}
	
	/**
	 * return Map<key, display value> for comobox
	 * 
	 * @param token                     token
	 * @param data                      data
	 * @return json                     json
	 * @throws Exception                exception
	 */
	public JsonNode queryResourceValue (
			String token,
			JsonNode data)
			throws Exception {
		
		if (data.get("kind").asText().contains("ConfigMap")) {
			return KubeClient.getClient(token).getResource(
					data.get("kind").asText(), 
					data.get("namespace").asText(), 
					data.get("name").asText()).get("data");
		} else {
			
			String fullKind = getFullKind(token, data.get("kind").asText());
			ArrayNode arrayNode = KubeClient.sqlMapper().queryAll(
					getTable(token, fullKind), data.get("field").asText());
			
			ObjectNode json = new ObjectMapper().createObjectNode();
			for (int i = 0; i < arrayNode.size(); i++) {
				json.put(arrayNode.get(i).asText(), arrayNode.get(i).asText());
			}
			return json;
		}
	}
	
	
	/*************************************************************************
	 * 
	 * 
	 *      Handling myself
	 * 
	 * 
	 **************************************************************************/
	
	
	/*************************************************************************
	 * 
	 * 
	 *       Common
	 * 
	 * 
	 **************************************************************************/
	/**
	 * @param token                      token
	 * @return json                      json
	 * @throws Exception                 exception
	 */
	protected JsonNode getComponents(
			 String token) throws Exception {
		
		Set<String> set = new HashSet<>();
		
		JsonNode navi = KubeClient.getClient(token).getResource(
								"Frontend", "default", "routes-admin")
								.get("spec").get("routes");
		
		for (int i = 0; i < navi.size(); i++) {
			JsonNode menu = navi.get(i).get("children");
			for (int j = 0; j < navi.size(); j++) {
				try {
					JsonNode list = menu.get(j).get("children");
					for (int k = 0; k < list.size(); k++) {
						set.add(list.get(k).get("name").asText());
					}
				} catch (Exception ex) {
					//
				}
			}
		}
		return new ObjectMapper().readTree(new ObjectMapper().writeValueAsBytes(set));
	}
	
	
	/**
	 * @param token                    token
	 * @param name                     name
	 * @return                         json
	 * @exception                      exception 
	 */
	protected JsonNode getTemplate(String token, String name) throws Exception {
		
		String userKind = getUserInputKind(name);
		
		String realKind = getKind(userKind);
				
		String fullKind = getFullKind(token, userKind);
			
		ObjectNode node = new ObjectMapper().createObjectNode();
		
		node.put("apiVersion", getApiVersion(token, fullKind));
		node.put("kind", realKind);
		
		ObjectNode meta = new ObjectMapper().createObjectNode();
		meta.put("name", realKind.toLowerCase() + "-" + StringUtil.getRandomString(6));
		
		node.set("metadata", meta);
		return node;
		
	}

	/**
	 * @param token                 token
	 * @param userInputKind         userInputKind
	 * @return                      kind
	 * @throws Exception            exception
	 */
	protected String getFullKind(String token, String userInputKind) throws Exception {
		return userInputKind.indexOf(".") == -1 ? KubeClient.getClient(token)
				.getAnalyzer().getConvertor().getRuleBase().getFullKind(userInputKind) : userInputKind;
	}

	/**
	 * @param userInputKind         kind
	 * @return                      kind
	 */
	protected String getKind(String userInputKind) {
		int rdx = userInputKind.lastIndexOf(".");
		return (rdx == -1) ? userInputKind : userInputKind.substring(rdx + 1);
	}

	/**
	 * @param name                  name
	 * @return                      kind
	 */
	protected String getUserInputKind(String name) {
		return name.substring(0, name.indexOf("-"));
	}
	
	/**
	 * @param token token
	 * @param kind  kind
	 * @return      apiVersion
	 * @throws Exception    exception
	 */
	protected String getApiVersion(
			String token,
			String kind) throws Exception {
		
		JsonNode mapper = KubeClient.getClient(token).getFullKinds();
		return mapper.has(kind) ? mapper.get(kind).get("apiVersion").asText() : "";
	}
	
	/**
	 * @param token                  token
	 * @param kind                   kind
	 * @return                       table
	 * @throws Exception             exception
	 */
	protected String getTable(String token, String kind) throws Exception {
		return KubeClient.getClient(token).getAnalyzer().getConvertor().getRuleBase().getName(kind);
	}
}
