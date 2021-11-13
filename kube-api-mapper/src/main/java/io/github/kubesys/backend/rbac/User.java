/**
 * Copyrigt (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.rbac;

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

}
