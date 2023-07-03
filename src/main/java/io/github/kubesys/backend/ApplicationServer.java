/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import io.github.kubesys.devfrk.spring.HttpServer;


/**
 * @author  wuheng@iscas.ac.cn
 * @version 1.2.0
 * @since   2023.06.28
 * 
 *        <p>
 *        启动backend服务，可以进一步对以下进行配置
 * 
 *        <li><code>src/main/resources/application.yml<code>
 *        <li><code>src/main/resources/log4j.properties<code>
 * 
 */

@ComponentScan(basePackages = { "io.github.kubesys.backend.services" })
public class ApplicationServer extends HttpServer  {
	
	/**
	 * 启动Backend服务
	 * 
	 * @param args 启动参数，默认是空
	 * @throws Exception 初始化启动失败报错即退出
	 */
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ApplicationServer.class, args);
		
	}

}
