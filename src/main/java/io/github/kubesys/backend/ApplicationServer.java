/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import io.github.kubesys.backend.clients.KubeMirrorClient;
import io.github.kubesys.backend.clients.SQLMapperClient;
import io.github.kubesys.client.KubernetesClient;
import io.github.kubesys.devfrk.spring.HttpServer;


/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @author xuyuanjia2017@otcaix.iscas.ac.cn
 * @since 2019.11.16
 * 
 *        <p>
 *        The {@code ApplicationServer} class is used for starting web
 *        applications. Please configure
 * 
 *        <li><code>src/main/resources/application.yml<code>
 *        <li><code>src/main/resources/log4j.properties<code>
 * 
 */

@ComponentScan(basePackages = { "io.github.kubesys.backend.services" })
public class ApplicationServer extends HttpServer  {

	/**
	 * program entry point
	 * 
	 * @param args default is null
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		KubernetesClient fromKube = new KubernetesClient();
		SQLMapperClient toSQL = new SQLMapperClient();
		
		KubeMirrorClient kubeMirror = new KubeMirrorClient(fromKube, toSQL);
		kubeMirror.start();
		
		SpringApplication.run(ApplicationServer.class, args);
	}

}
