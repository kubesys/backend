/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.util.logging.Logger;

import io.github.kubesys.client.KubernetesClient;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @since  2.0.0
 * 
 * This class is used for checking runtime-mirror's availability,
 * and recover it if necessary.
 * 
 **/
public class KubePinger extends Thread {

	public final static Logger m_logger = Logger.getLogger(KubePinger.class.getName());
	
	protected final KubernetesClient client;
	
	public KubePinger(KubernetesClient kubeClient) {
		super();
		this.client = kubeClient;
	}

	@Override
	public void run() {
		while (true) {
			try {
				checkAvailPeriod(10000);
			} catch (Exception ex) {
				recoverByRestart(ex);
			}
		}
	}

	private void recoverByRestart(Exception ex) {
		m_logger.severe("unable to connect to Kubernetes: " + ex);
		System.exit(1);
	}

	private void checkAvailPeriod(long ms) throws Exception, InterruptedException {
		client.getResource("Namespace", "default")
						.get("metadata").get("name");
		Thread.sleep(ms);
		m_logger.info("finish availability check...");
	}
}
