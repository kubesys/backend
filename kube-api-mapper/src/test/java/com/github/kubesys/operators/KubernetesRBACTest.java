/**
 * Copyright (2020, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.operators;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.kubesys.kubeclient.KubernetesClient;
import io.github.kubesys.kubeclient.KubernetesConstants;
import io.github.kubesys.kubeclient.KubernetesWatcher;


/**
 * @author wuheng09@gmail.com
 *
 */
public class KubernetesRBACTest {

	
	public static void main(String[] args) throws Exception {
		
		KubernetesClient all = new KubernetesClient("https://39.100.71.73:6443", 
				"eyJhbGciOiJSUzI1NiIsImtpZCI6IjJqbW9qdGxjdzkyUmpDUWxGbkJXdVJfUFY0c0NuYzVpOG9SN1Rla3E2eXcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWNsaWVudC10b2tlbi00cmx0NyIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWNsaWVudCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjQ3NTBiYzUyLWY5ZTctNDQ5OS1iNTMwLTMyNjNlZDJiZmE5MyIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWNsaWVudCJ9.dgwfUziRsfdqxrPE0Ba_9N5fR3PHd8tBDO4q7vBbRUSMhlkY76fICI8eIDeoe3LnWdH3Eo5RxfOYfMSJA0qcUvEO2zM0q1-GIl4AV0svpvVp5-BbunDOjWYmOtBFnkSIQcXSwmhhBpBaGOeVtJCUS4dCkgfu_oJZRYc2glFGfVQ1b-dfR6gajMugcjmeH-a7bAzvpzHqFoNSV8ib1fiSONeeQGg13GYnenEwtbYp2wGfKsIXcWnw0u5Q9sU2ogEZrfkD2PYpoZD7b0dyZW-wYwvotRNWnCAEmASTzi2YYO49FDv3qRGsU4SWfE_TkBb9oQxhckkkCBtyu7WihkWjbg");
		
		KubernetesClient limited = new KubernetesClient("https://39.100.71.73:6443", 
				"eyJhbGciOiJSUzI1NiIsImtpZCI6IjJqbW9qdGxjdzkyUmpDUWxGbkJXdVJfUFY0c0NuYzVpOG9SN1Rla3E2eXcifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImFkbWluLXRva2VuLXRqZmg1Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiMTNkYjY3N2EtYTdkMS00M2FmLTk0N2YtMzllNTAzYzFjMzQ4Iiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmRlZmF1bHQ6YWRtaW4ifQ.k03Jp9NwC9a2uiTNvzQ5QTW8C7JD3IY6SLRgcreIsepqrEq1qXo-7rg0H5Sa98zL29iQyIiAgbg85ap5i63TK__f6_IZ_GZv4viSMofQs4yi_2PASszvGuZOteznjUxvPi0RtirQcffedxBYdvOQE_UJSHc5Gy9TzWLEtTTNskHKqwizcNjFZN1bs793C4Ipf0Ga3ux3AE9FIuqGVMeBh23of9nLzOrJT7lLtAusi4cBdhzI4v33aVwX5QnUtBOfRccWA3CAOWN2EzJA0hIJRZEzum6iKOTtJ106IeqlHWgFdbUl", 
				all.getAnalyzer());
		
		KubernetesWatcher watcher = new KubernetesWatcher(limited) {
			
			@Override
			public void doModified(JsonNode node) {
				System.out.println(node);
			}
			
			@Override
			public void doDeleted(JsonNode node) {
				System.out.println(node);
			}
			
			@Override
			public void doAdded(JsonNode node) {
				System.out.println(node);
			}

			@Override
			public void doClose() {
				System.out.println("close");
			}

		};
		limited.watchResources("Namespace", KubernetesConstants.VALUE_ALL_NAMESPACES, watcher);
	
	}


}
