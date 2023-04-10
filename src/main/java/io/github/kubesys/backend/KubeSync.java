/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import io.github.kubesys.backend.clients.KubeMirrorClient;
import io.github.kubesys.backend.clients.SQLMapperClient;
import io.github.kubesys.client.KubernetesClient;


public class KubeSync  {

	public static void main(String[] args) throws Exception {
		KubernetesClient fromKube = new KubernetesClient(
				System.getenv("kubeUrl"), System.getenv("kubeToken"));
		
		KubeMirrorClient kubeMirror = new KubeMirrorClient(fromKube, new SQLMapperClient());
		new Thread(kubeMirror).start();
		
	}

}
