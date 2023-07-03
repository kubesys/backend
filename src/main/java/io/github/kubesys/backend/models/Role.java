/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.models;


import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author wuheng@iscas.ac.cn
 * @since  0.1.0
 * @date   2023/05/22
 * 
 */
@Entity
@Table(name = "basic_role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseModel   {

	@Id
	@Column(name = "role", length = 32)
    private Long role;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "tokens", columnDefinition = "json")
    private JsonNode tokens;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "allows", columnDefinition = "json")
    private JsonNode allows;

	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}

	public JsonNode getTokens() {
		return tokens;
	}

	public void setTokens(JsonNode tokens) {
		this.tokens = tokens;
	}

	public JsonNode getAllows() {
		return allows;
	}

	public void setAllows(JsonNode allows) {
		this.allows = allows;
	}
	
}
