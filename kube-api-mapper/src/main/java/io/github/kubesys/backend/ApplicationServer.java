/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import com.github.kubesys.httpfrk.HttpServer;


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
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationServer.class, args);
	}

	@Override
	public String getTitle() {
		return "面向云边端、人机物融合的共性基础设施平台";
	}

	@Override
	public String getDesc() {
		return "基于Kubernetes支持云边端各种场景，系统当前支持:<br>"
				+ "(1)私有云管理平台：  KVM、OpenStack、vCenter 6.x<br>"
				+ "(2)混合云管理平台：  Amazon、Azure、阿里云、百度云、腾讯云、华为云的虚拟机、容器、块存储服务,<br>"
				+ "(3)微服务管控平台：  istio、SpringCloud<br>"
				+ "(4)大数据调度平台：  Spark、Hadoop、Hive、Tensoflow,<br>"
				+ "(5)人工智能算法平台： Tendorflow、Pytorch,<br>"
				+ "(4)无服务器计算平台： OpenFaaS<br>";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String getPackage() {
		return "io.github.kubesys.backend";
	}

}
