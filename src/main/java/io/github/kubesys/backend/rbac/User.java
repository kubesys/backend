/**
 * Copyrigt (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.rbac;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author wuheng@iscas.ac.cn
 * @since  2.0.4
 *
 */
public class User {

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

	public JsonNode toJson() {
		ObjectNode userJson = new ObjectMapper().createObjectNode();
		
		userJson.put("apiVersion", "doslab.io/v1");
		userJson.put("kind", "User");
		ObjectNode meta = new ObjectMapper().createObjectNode();
		{
			meta.put("name", name);
			meta.put("namespace", "default");
		}
		userJson.set("metadata", meta);

		ObjectNode spec = new ObjectMapper().createObjectNode();
		spec.put("password", password);
		spec.put("role", role);
		userJson.set("spec", spec);
		
		return userJson;
	}
}
