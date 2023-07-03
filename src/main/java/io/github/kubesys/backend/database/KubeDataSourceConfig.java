/**
 * Copyright (2023, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend.database;

import javax.sql.DataSource;

import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * @author wuheng@iscas.ac.cn
 * @version 2.3.0
 * @since 2023/07/03
 * 
 */
@Configuration
@EnableTransactionManagement
public class KubeDataSourceConfig {

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.kube")
	public DataSource firstDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean firstEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("kubeDataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		return entityManagerFactoryBean;
	}

	@Primary
	@Bean
	public EntityManager firstEntityManager(
			@Qualifier("kubeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return entityManagerFactory.createEntityManager();
	}
}
