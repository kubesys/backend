/**
 * Copyrigt (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.rbac;

/**
 * @author wuheng@iscas.ac.cn
 * @since 2.0.4
 *
 */
public class Role {

	protected String name;

	protected Rule[] rules;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Rule[] getRules() {
		return rules;
	}

	public void setRules(Rule[] rules) {
		this.rules = rules;
	}

	public static class Rule {

		protected String[] apiGroups;
		
		protected String[] verbs;
		
		protected String[] resources;

		public String[] getApiGroups() {
			return apiGroups;
		}

		public void setApiGroups(String[] apiGroups) {
			this.apiGroups = apiGroups;
		}

		public String[] getVerbs() {
			return verbs;
		}

		public void setVerbs(String[] verbs) {
			this.verbs = verbs;
		}

		public String[] getResources() {
			return resources;
		}

		public void setResources(String[] resources) {
			this.resources = resources;
		}

			
	}

}
