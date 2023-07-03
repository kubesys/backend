/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "basic_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseModel   {

	@Id
	@Column(name = "name", length = 32)
    private Long name;
	
	@Column(name = "password", length = 32)
    private Long password;

	@Column(name = "role", length = 32)
    private Long role;

	public Long getName() {
		return name;
	}

	public void setName(Long name) {
		this.name = name;
	}

	public Long getPassword() {
		return password;
	}

	public void setPassword(Long password) {
		this.password = password;
	}

	public Long getRole() {
		return role;
	}

	public void setRole(Long role) {
		this.role = role;
	}
	
}
