/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author wuheng@iscas.ac.cn
 * @since  2.0.0
 */
public class KubeUtils {

	
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
	 * @param json                           json
	 * @return                               name
	 */
	public static String getName(JsonNode json) {
		return json.get(YAML_METADATA)
					.get(YAML_METADATA_NAME).asText();
	}
	
	/**
	 * @param json                           json
	 * @param namespaced                     true or false
	 * @return                               namespace
	 */
	public static String getNamespace(JsonNode json, boolean namespaced) {
		JsonNode meta = json.get(YAML_METADATA);
		return (meta.has(YAML_METADATA_NAMESPACE)) 
				? meta.get(YAML_METADATA_NAMESPACE).asText() : (namespaced ? "default" : "");
	}

	/**
	 * @param json                           json
	 * @return                               namespace
	 */
	public static String getNamespace(JsonNode json) {
		JsonNode meta = json.get(YAML_METADATA);
		return (meta.has(YAML_METADATA_NAMESPACE)) 
				? meta.get(YAML_METADATA_NAMESPACE).asText() : "default";
	}
	
	
//	public static void createMeatadata(KubernetesClient client, String fullkind) {
//		KubernetesRuleBase ruleBase = client.getAnalyzer()
//				.				getConvertor().getRuleBase();
//		
//		ObjectNode json = new ObjectMapper().createObjectNode();
//		json.put("apiVersion", "doslab.io/v1");
//		json.put("kind", "Metadata");
//		
//		ObjectNode meta = new ObjectMapper().createObjectNode();
//		meta.put("name", fullkind.toLowerCase());
//		json.set("metadata", meta);
//		
//		ObjectNode spec = new ObjectMapper().createObjectNode();
//		spec.put("kind", ruleBase.getKind(fullkind));
//		spec.put("fullkind", fullkind);
//		spec.put("group", ruleBase.getGroup(fullkind));
//		spec.put("version", ruleBase.getVersion(fullkind));
//		spec.put("api", ruleBase.getApiPrefix(fullkind));
//		spec.put("namespaced", ruleBase.isNamespaced(fullkind) ? "Namespace" : "Cluster");
//		
//		// "create", "delete", "deletecollection", "get", "list", "patch", "update", "watch" 
//		ArrayNode verbs = new ObjectMapper().createArrayNode();
//		verbs.add("create");
//		verbs.add("delete");
//		verbs.add("deletecollection");
//		verbs.add("get");
//		verbs.add("list");
//		verbs.add("patch");
//		verbs.add("update");
//		verbs.add("watch");
//		
//		spec.set("verbs", verbs);
//		json.set("spec", spec);
//		
//		try {
//			client.createResource(json);
//		} catch (Exception e) {
//		}
//	}
}
