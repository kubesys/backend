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
		
		KubernetesClient all = new KubernetesClient("https://39.106.40.190:6443", 
				"all-permission-token");
		
		KubernetesClient limited = new KubernetesClient("https://39.106.40.190:6443", 
				"your-token", 
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
