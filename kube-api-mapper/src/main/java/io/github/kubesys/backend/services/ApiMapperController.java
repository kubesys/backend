/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kubesys.httpfrk.core.HttpController;
import com.github.kubesys.httpfrk.core.HttpResponseWrapper;

import io.github.kubesys.backend.utils.KubeUtil;


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

@RestController
@ComponentScan
public class ApiMapperController extends HttpController  {

//	public static Properties props = new Properties();
//	
//	static {
//		try {
//			props.load(new FileInputStream(ResourceUtils.getFile("logInfo")));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	protected String doResponse(String servletPath, JsonNode body) throws Exception {
		m_logger.info("Begin to deal with " + servletPath);
		long start = System.currentTimeMillis();
		Method hanlder = handlers.geHandler(servletPath);
		try {

			Object[] params = getParameters(body, hanlder);
			Object result = (params != null) ? hanlder.invoke(getInstance(servletPath), params)
					: hanlder.invoke(getInstance(servletPath));

			m_logger.info("Successfully deal with " + servletPath);
			return ((HttpResponseWrapper) ctx.getBean("wrapper"))
							.unwrap("success", result);
		} catch (Exception ex) {
			StringBuffer sb = new StringBuffer();
			if (ex instanceof InvocationTargetException) {
				sb.append(((InvocationTargetException) ex).getTargetException());
			} else {
				sb.append(ex.getMessage()).append("\n");
				for (StackTraceElement ste : ex.getStackTrace()) {
					sb.append("\t").append(ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber()).append("\n");
				}
			}
			throw new Exception(sb.toString());
		} finally {
			try {
				if (body.has("data")) {
					KubeUtil.log("default", servletPath, body.get("data").get("kind").asText(), body.get("data").get("name").asText(), start);
				} else {
					KubeUtil.log("default", servletPath, body.get("kind").asText(), body.get("name").asText(), start);
				}
			} catch (Exception ex) {
				
			}
		}
	}
}
