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
		
		// is ConfigMap
		if (node.has(YAML_CONFIGMAP_DATA)) {
			node.remove(YAML_CONFIGMAP_DATA);
		}
		
//		value = toMysqlJSON(value, "&", "\\u0026");
//		value = toMysqlJSON(value, ">", "\\u003e");
//		value = toMysqlJSON(value, "<", "\\u003c");
//		value = toMysqlJSON(value, "\'", " \\'");
//		value = toMysqlJSON(value, "\\\"", "\\\\\\\"");
//		value = toMysqlJSON(value, "\\n", "\\\\n");
//		value = toMysqlJSON(value, "\\\\\\\\\\\"", "\\\\\\\"");
//		value = toMysqlJSON(value, "\\\\d", "\\\\\\\\d");
		
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
}
