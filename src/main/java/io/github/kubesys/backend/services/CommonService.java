/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.services;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.kubesys.devfrk.spring.cores.AbstractHttpHandler;
import io.github.kubesys.devfrk.tools.annotations.ServiceDefinition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * @author wuheng@iscas.ac.cn
 * @since 0.2.0
 * @date 2023/05/29
 * 
 */
@ServiceDefinition
public class CommonService extends AbstractHttpHandler {

	private static Logger m_logger = Logger.getLogger(CommonService.class);

	private static final Map<String, String> tables = new HashMap<>();


	@PersistenceContext(unitName = "authEntityManagerFactory")
    private EntityManager authEntityManager;

    @PersistenceContext(unitName = "kubeEntityManagerFactory")
    private EntityManager kubeEntityManager;

//	@Transactional
//	void createSpecifiedObject(Object obj) throws Exception {
//		entityManager.persist(obj);
//	}
//
//	@Transactional
//	public void createObject(String cls, JsonNode data) throws Exception {
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.registerModule(new JavaTimeModule());
//		Object treeToValue = objectMapper.treeToValue(data, Class.forName(cls));
//		entityManager.persist(treeToValue);
//	}
//
//	@Transactional
//	void updateSpecifiedObject(Object obj) throws Exception {
//		entityManager.merge(obj);
//	}
//
//	@Transactional
//	public void updateObject(String cls, JsonNode data) throws Exception {
//		Object treeToValue = new ObjectMapper().treeToValue(data, Class.forName(cls));
//		updateSpecifiedObject(treeToValue);
//	}
//
//	@Transactional
//	void deleteSpecifiedObject(Object obj) throws Exception {
//		entityManager.remove(obj);
//	}
//
//	@Transactional
//	public void deleteObject(String cls, JsonNode data) throws Exception {
//		Object treeToValue = new ObjectMapper().treeToValue(data, Class.forName(cls));
//		deleteSpecifiedObject(treeToValue);
//	}

}
