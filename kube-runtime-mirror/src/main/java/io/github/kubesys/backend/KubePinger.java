/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import io.github.kubesys.kubeclient.KubernetesClient;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2.0.0
 * 
 **/
public class KubePinger extends Thread {

	protected final KubernetesClient kubeClient;
	
	public KubePinger(KubernetesClient kubeClient) {
		super();
		this.kubeClient = kubeClient;
	}

	@Override
	public void run() {
		while (true) {
			try {
				kubeClient.getResource("Namespace", "default");
				Thread.sleep(10000);
			} catch (Exception ex) {
				System.exit(1);
			}
		}
	}
	
	
	
}
