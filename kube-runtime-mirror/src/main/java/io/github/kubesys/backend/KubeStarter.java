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
		
		KubernetesClient fromKube = new KubernetesClient();
		
		SQLMapper toSQL = new SQLMapper();
		
		KubeMirror kubeMirror = new KubeMirror(fromKube, toSQL);
		kubeMirror.start();
		
		KubePinger kubePinger = new KubePinger(fromKube);
		kubePinger.start();
		
	}
	
}
