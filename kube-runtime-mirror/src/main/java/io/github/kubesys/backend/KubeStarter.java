/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;


import io.github.kubesys.client.KubernetesClient;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2.0.0
 * 
 **/
public class KubeStarter {
	
	
	/*****************************************************************************************
	 * 
	 * Main
	 * 
	 *****************************************************************************************/

	/**
	 * @param args                               args
	 * @throws Exception                         exception
	 */
	public static void main(String[] args) throws Exception {
		
		KubernetesClient kubeClient = new KubernetesClient();
		
		KubeMirror kubeMirror = new KubeMirror(kubeClient);
		kubeMirror.start();
		
		KubePinger kubePinger = new KubePinger(kubeClient);
		kubePinger.start();
		
	}
	
}
