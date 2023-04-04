/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.utils;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.kubesys.client.KubernetesClient;

/**
 * @author lichengzhi99@otcaix.iscas.ac.cn
 * @author wuheng@otcaix.iscas.ac.cn
 *
 */
public class KubeUtil {

	/**
	 * m_logger
	 */
	protected final static Logger m_logger = Logger.getLogger(KubeUtil.class.getName());
	
	public static final String YAML_METADATA                        = "metadata";
	
	public static final String YAML_METADATA_NAME                   = "name";
	
	public static final String YAML_METADATA_NAMESPACE              = "namespace";
	
	public static final String YAML_METADATA_ANNOTATIONS            = "annotations";
	
	public static final String YAML_METADATA_MANAGEDFIELDS          = "managedFields";
	
	public static final String YAML_CONFIGMAP_DATA                  = "data";
	
	/**
	 * @param json                            json
	 * @return                                node
	 * @throws Exception 
	 */
	public static String getJsonWithoutAnotation(JsonNode json) throws Exception {
		ObjectNode node = json.deepCopy();
		ObjectNode meta = node.get(YAML_METADATA).deepCopy();
		
		if (meta.has(YAML_METADATA_ANNOTATIONS)) {
			meta.remove(YAML_METADATA_ANNOTATIONS);
		}
		
		// > 1.18
		if (meta.has(YAML_METADATA_MANAGEDFIELDS)) {
			meta.remove(YAML_METADATA_MANAGEDFIELDS);
		}
		
		node.remove(YAML_METADATA);
		node.set(YAML_METADATA, meta);
		
		return toJSON(node.toPrettyString(), "\'", "");
	}

	@Deprecated
	public static String toJSON(String value, String src, String dst) {
		StringBuilder sb = new StringBuilder();
		int i = value.indexOf(src);
		if (i == -1) {
			sb.append(value);
		} else {
			sb.append(value.substring(0, i)).append(dst).append(
					toJSON(value.substring(i + src.length()), src, dst));
		}
		return sb.toString();
	}
	
	/**
	 * @param json json
	 * @return kind
	 * @throws Exception exception
	 */
	public static String getKind(JsonNode json) throws Exception {
		return json.get("kind").asText();
	}

	/**
	 * @param json json
	 * @return kind
	 * @throws Exception exception
	 */
	public static String getName(JsonNode json) throws Exception {
		return json.get("metadata").get("name").asText();
	}

	/**
	 * @param json json
	 * @return kind
	 * @throws Exception exception
	 */
	public static String getNamespace(JsonNode json) throws Exception {
		return json.get("metadata").get("namespace").asText();
	}

	/**
	 * @param name      name
	 * @return json
	 */
	public static ObjectNode createServiceAccount(String name) {

		ObjectNode serviceaccount = new ObjectMapper().createObjectNode();
		serviceaccount.put("apiVersion", "v1");
		serviceaccount.put("kind", "ServiceAccount");
		ObjectNode serviceaccountMeta = new ObjectMapper().createObjectNode();
		{
			serviceaccountMeta.put("name", name);
			serviceaccountMeta.put("namespace", "default");
		}
		serviceaccount.set("metadata", serviceaccountMeta);

		return serviceaccount;
	}

	/**
	 * @param name  name
	 * @param rules rules
	 * @return json
	 */
	public static ObjectNode createClusterRole(String name, JsonNode rules) {

		ObjectNode clusterrole = new ObjectMapper().createObjectNode();
		clusterrole.put("apiVersion", "rbac.authorization.k8s.io/v1");
		clusterrole.put("kind", "ClusterRole");
		ObjectNode clusterroleMeta = new ObjectMapper().createObjectNode();
		{
			clusterroleMeta.put("name", name);
		}
		clusterrole.set("metadata", clusterroleMeta);
		clusterrole.set("rules", rules);

		return clusterrole;
	}

	/**
	 * @param name      name
	 * @return json
	 */
	public static ObjectNode createClusterRoleBinding(String name) {
		ObjectNode clusterrolebinding = new ObjectMapper().createObjectNode();
		clusterrolebinding.put("apiVersion", "rbac.authorization.k8s.io/v1");
		clusterrolebinding.put("kind", "ClusterRoleBinding");
		ObjectNode clusterrolebindingMeta = new ObjectMapper().createObjectNode();
		{
			clusterrolebindingMeta.put("name", name);
			clusterrolebinding.set("metadata", clusterrolebindingMeta);
		}
		ObjectNode clusterrolebindingRoleRef = new ObjectMapper().createObjectNode();
		{
			clusterrolebindingRoleRef.put("apiGroup", "rbac.authorization.k8s.io");
			clusterrolebindingRoleRef.put("kind", "ClusterRole");
			clusterrolebindingRoleRef.put("name", name);
		}
		clusterrolebinding.set("roleRef", clusterrolebindingRoleRef);
		ArrayNode clusterrolebindingSubjects = new ObjectMapper().createArrayNode();
		ObjectNode clusterrolebindingSubject = new ObjectMapper().createObjectNode();
		{
			clusterrolebindingSubject.put("kind", "ServiceAccount");
			clusterrolebindingSubject.put("name", name);
			clusterrolebindingSubject.put("namespace", "default");
		}
		clusterrolebindingSubjects.add(clusterrolebindingSubject);
		clusterrolebinding.set("subjects", clusterrolebindingSubjects);
		return clusterrolebinding;
	}

	/**
	 * @param client    client
	 * @param namespace namespace
	 * @param roleName  roleName
	 * @return token
	 * @throws Exception exception
	 */
	public static String getToken(KubernetesClient client, String namespace, String roleName) throws Exception {

		try {
			String roleAccount = client.getResource("ServiceAccount", namespace, roleName).get("secrets").get(0)
					.get("name").asText();

			String roleToken = client.getResource("Secret", namespace, roleAccount).get("data").get("token").asText();
			return new String(Base64.getDecoder().decode(roleToken.getBytes()));
		} catch (Exception ex) {
			throw new Exception("Please create userrole [" + roleName + "] in namespace [" + namespace + "] first.");
		}
	}

	public static void log(String token, String url, String kind, String name, long start) throws Exception {

		long end = System.currentTimeMillis();

		ObjectNode log = new ObjectMapper().createObjectNode();
		if (!kind.equals("Log") && !kind.equals("RegExp") && !kind.equals("Frontend")) {
			log.put("apiVersion", "doslab.io/v1");
			log.put("kind", "Log");

			{
				ObjectNode meta = new ObjectMapper().createObjectNode();
				meta.put("name", "log" + StringUtil.getRandomString(6));
				log.set("metadata", meta);
			}
			{
				ObjectNode spec = new ObjectMapper().createObjectNode();
				spec.put("user", getUser(token));
				spec.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start));
				spec.put("start", start);
				spec.put("end", end);
				spec.put("duration", (end - start) + "ms");
				spec.put("url", url);
				spec.put("type", kind);
				spec.put("resource", name);
				log.set("spec", spec);
			}
		}
		
		try {
			
			ClientUtil.getClient("default").createResource(log);
		} catch (Exception ex) {
			m_logger.info(log.toPrettyString());
		}
	}

	public static String getUser(String token) {
		int idx = token.indexOf("-");
		return (idx == -1) ? "admin" : token.substring(0, idx);
	}

}
