/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.clients;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * @author wuheng@iscas.ac.cn
 * @since 0.2.0
 * @date 2023/05/29
 * 
 */
@Component
public class PostgresPoolClient {

	private static Logger m_logger = Logger.getLogger(PostgresPoolClient.class);

	private static final Map<String, String> tables = new HashMap<>();


	@PersistenceContext(unitName = "authEntityManager")
    private EntityManager authEntityManager;

    @PersistenceContext(unitName = "kubeEntityManager")
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
