/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services.commons;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kubesys.httpfrk.core.HttpBodyHandler;
import com.github.kubesys.tools.annotations.ServiceDefinition;

import io.github.kubesys.backend.utils.ClientUtil;
import io.github.kubesys.backend.utils.KubeUtil;
import io.github.kubesys.backend.utils.StringUtil;
import io.github.kubesys.kubeclient.KubernetesConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @author chace
 * @since 2019.10.29
 *
 */

@ServiceDefinition
@Api(value = "基于Kubernetes规范的资源生命周期管理")
public class KubeService extends HttpBodyHandler {

	/**
	 * @param token token
	 * @param json  json
	 * @return json
	 * @throws Exception exception
	 */
	@ApiOperation(value = "创建基于Kubernetes规范的资源，可以是自定义资源CRD")
	public JsonNode createResource(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token,
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}") JsonNode json)
			throws Exception {

		long start = System.currentTimeMillis();
		
		try {
			return ClientUtil.getClient(token).createResource(json);
		} catch (Exception ex) {
			throw new Exception("创建" + getKindByJson(json) + "资源" + getName(json)  + "失败, 不符合Kubernetes语法.");
		} finally {
			KubeUtil.log(token, "/kube/createResource", getKindByJson(json), getName(json), start);
		}

	}

	/**
	 * @param token token
	 * @param json  json
	 * @return json
	 * @throws Exception exception
	 */
	@ApiOperation(value = "更新基于Kubernetes规范的资源，可以是自定义资源CRD")
	public JsonNode updateResource(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token,
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}") JsonNode json)
			throws Exception {
		
		long start = System.currentTimeMillis();
		
		try {
			return ClientUtil.getClient(token).updateResource(json);
		} catch (Exception ex) {
			throw new Exception("更新" + getKindByJson(json) + "资源" + getName(json) + "失败, 包含无法更新的字段.");
		} finally {
			KubeUtil.log(token, "/kube/updateResource", getKindByJson(json), getName(json), start);
		}
	}

	/**
	 * @param token token
	 * @param json  json
	 * @return json
	 * @throws Exception exception
	 */
	@ApiOperation(value = "创建或者更新基于Kubernetes规范的资源，可以是自定义资源CRD")
	public JsonNode createOrUpdateResource(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token,
			@ApiParam(value = "基于Kubernetes规范的资源描述", required = true, example = "{\"apiVersion\": \"v1\" ,\"kind\" : \"Pod\"}") JsonNode json)
			throws Exception {
		
		long start = System.currentTimeMillis();
		
		try {
			try {
				return ClientUtil.getClient(token).createResource(json);
			} catch (Exception e) {
				return ClientUtil.getClient(token).updateResource(json);
			}
		} catch (Exception ex) {
			throw new Exception("强制创建资源失败, 不符合Kubernetes语法或者字段无法更新.");
		} finally {
			KubeUtil.log(token, "/kube/createOrUpdateResource", getKindByJson(json), getName(json), start);
		}
	}

	/**
	 * @param token token
	 * @param json  json
	 * @return json
	 * @throws Exception exception
	 */
	@ApiOperation(value = "删除基于Kubernetes规范的资源，可以是自定义资源CRD")
	public JsonNode deleteResource(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token,
			@ApiParam(value = "注册到Kubernetes类型", required = true, example = "Pod") String kind,
			@ApiParam(value = "命名空间", required = true, example = "字符串或者\"\"") String namespace,
			@ApiParam(value = "资源名", required = true, example = "test") String name) throws Exception {

		long start = System.currentTimeMillis();
		
		try {
			return ClientUtil.getClient(token).deleteResource(kind, namespace, name);
		} catch (Exception ex) {
			throw new Exception("删除资源失败, 命名空间" + namespace + "类型为" + kind + "的资源" + name +"不存在. ");
		} finally {
			KubeUtil.log(token, "/kube/deleteResource", kind, name, start);
		}
	}

	/**
	 * @param token     token
	 * @param kind      kind
	 * @param namespace namespace
	 * @param name      name
	 * @return json
	 * @throws Exception exception
	 */
	@ApiOperation(value = "获取基于Kubernetes规范的资源，可以是自定义资源CRD")
	public JsonNode getResource(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token,
			@ApiParam(value = "注册到Kubernetes类型", required = true, example = "Pod") String kind,
			@ApiParam(value = "命名空间", required = true, example = "字符串或者\"\"") String namespace,
			@ApiParam(value = "资源名", required = true, example = "test") String name) throws Exception {
		
		long start = System.currentTimeMillis();
		
		try {
			return namespace == null ? ClientUtil.getClient(token).getResource(kind, name.toLowerCase())
					: ClientUtil.getClient(token).getResource(kind, namespace, name.toLowerCase());
		} catch (Exception ex) {
			if ((kind.equals("Template") || kind.equals("doslab.io.Template")) && name.endsWith("create")) {
				return getTemplate(token, name); 
			}
			throw new Exception("获取创建资源失败, 命名空间" + namespace + "类型为" + kind + "的资源" + name +"不存在. ");
		} finally {
			KubeUtil.log(token, "/kube/getResource", kind, name, start);
		}
	}

	/**
	 * @param token     token
	 * @param kind      kind
	 * @param namespace namespace
	 * @return json
	 * @throws Exception exception
	 */
	@ApiOperation(value = "按需查询基于Kubernetes规范的资源，可以是自定义资源CRD")
	public JsonNode listResources(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token,
			@ApiParam(value = "注册到Kubernetes类型", required = true, example = "Pod") String kind,
			@ApiParam(value = "每页显示多少数据", required = true, example = "10") int limit,
			@ApiParam(value = "显示第几页的数据", required = true, example = "1") int page,
			@ApiParam(value = "查询条件", required = false, example = "根据json格式，如<metadata#name,henry>") Map<String, String> labels)
			throws Exception {

		long start = System.currentTimeMillis();

		try {
			try {
				// check permissions
				ClientUtil.getClient(token).listResources(kind, KubernetesConstants.VALUE_ALL_NAMESPACES, null, null, 1, null);
			} catch (Exception ex) {
				// sql connection may timeout, retry it
				throw new Exception("没有权限, 无法操作资源类型 " + kind + ". ");
			}
			
			return ClientUtil.sqlClient().query(getTable(token, getFullKind(token, kind)), 
											getKind(kind), 
											limit, page, labels != null ? labels : null);
		} catch (Exception ex) {
			throw new Exception("查询资源失败, 数据库宕机或者数据库中不存类型" + kind+ "对应的数据表.");
		} finally {
			KubeUtil.log(token, "/kube/listResources", kind, "all", start);
		}
	}

	/**
	 * @param token       token
	 * @return            json
	 * @throws Exception 
	 */
	public JsonNode getComponents(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token
			) throws Exception {
		
		Set<String> set = new HashSet<>();
		
		JsonNode navi = ClientUtil.getClient(token).getResource(
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
	 * @param token       token
	 * @return            json
	 * @throws Exception 
	 */
	public JsonNode getKinds(
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token
			) throws Exception {
		
		return ClientUtil.getClient(token).getFullKinds();
	}
	
	/*********************************************************
	 *    
	 *    
	 *    Deprecated
	 *
	 *
	 *********************************************************/
	
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
	 * @param json                  json
	 * @return                      name
	 */
	protected String getName(JsonNode json) {
		return json.get("metadata").get("name").asText();
	}

	/**
	 * @param json                  json
	 * @return                      kind
	 */
	protected String getKindByJson(JsonNode json) {
		return json.get("kind").asText();
	}
	
	/**
	 * @param token                 token
	 * @param userInputKind         userInputKind
	 * @return                      kind
	 * @throws Exception            exception
	 */
	protected String getFullKind(String token, String userInputKind) throws Exception {
		return userInputKind.indexOf(".") == -1 ? ClientUtil.getClient(token)
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
			@ApiParam(value = "权限凭证，关联到Kubernetes的Secret", required = true, example = "5kh562.a1sagksdyyk6ivs1") String token,
			@ApiParam(value = "注册到Kubernetes类型", required = true, example = "Pod") String kind) throws Exception {
		
		JsonNode mapper = ClientUtil.getClient(token).getFullKinds();
		return mapper.has(kind) ? mapper.get(kind).get("apiVersion").asText() : "";
	}
	
	/**
	 * @param token                  token
	 * @param kind                   kind
	 * @return                       table
	 * @throws Exception             exception
	 */
	protected String getTable(String token, String kind) throws Exception {
		return ClientUtil.getClient(token).getAnalyzer().getConvertor().getRuleBase().getName(kind);
	}
}
