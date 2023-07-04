/**
 * Copyright (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package io.github.kubesys.backend;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.github.kubesys.backend.models.auth.AuthBaseModel;
import io.github.kubesys.backend.models.kube.KubeBaseModel;
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

@ComponentScan(basePackages = { "io.github.kubesys.backend.services", "io.github.kubesys.backend.clients" })
@EntityScan(basePackages = { "io.github.kubesys.backend.models"})
@EnableTransactionManagement 
@EnableJpaRepositories
@EnableConfigurationProperties(JpaProperties.class)
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
	
	/*******************************************************
	 * 
	 * 配置两个数据源，见application.yml
	 * ChatGPT告诉我的，不要问我为啥？
	 * 
	 ********************************************************/
	/**
	 * 配置application.yml中spring.jpa
	 * 
	 * @return JpaProperties
	 */
	@Bean(name = "jpa")
    @ConfigurationProperties(prefix = "spring.jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }
	
	@Bean(name = "jpaProperties")
    public Properties hibernateProperties(@Qualifier("jpa") JpaProperties jpaProperties) {
        Properties properties = new Properties();
        properties.putAll(jpaProperties.getProperties());
        return properties;
    }
	
	@Bean(name = "authDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.auth")
	public DataSource authDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "kubeDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.kube")
	public DataSource kubeDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "jpaSharedEM_entityManagerFactory")
	@DependsOn({"jpaProperties", "authDataSource"})
    public LocalContainerEntityManagerFactoryBean  authEntityManager(
                    @Qualifier("authDataSource") DataSource dataSource,
                    @Qualifier("jpaProperties") Properties properties) {
        
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = 
							new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPersistenceUnitName("authEntityManager");
		entityManagerFactoryBean.setPackagesToScan(AuthBaseModel.class.getPackageName());
		entityManagerFactoryBean.setJpaProperties(properties);
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean.afterPropertiesSet();
		return entityManagerFactoryBean;
		
    }

    @Bean(name = "kubeEntityManagerFactory")
    @DependsOn({"jpaProperties", "kubeDataSource"})
    public LocalContainerEntityManagerFactoryBean  kubeEntityManager(
                      @Qualifier("kubeDataSource") DataSource dataSource,
                      @Qualifier("jpaProperties") Properties properties) {
    	LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = 
    							new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPersistenceUnitName("kubeEntityManager");
		entityManagerFactoryBean.setPackagesToScan(KubeBaseModel.class.getPackageName());
		entityManagerFactoryBean.setJpaProperties(properties);
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean.afterPropertiesSet();
		return entityManagerFactoryBean;
    }
    
    @Bean(name = "kubeEntityManager")
    public PlatformTransactionManager kubeTransactionManager(
    		@Qualifier("kubeDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean(name = "authEntityManager")
    public PlatformTransactionManager authTransactionManager(
    		@Qualifier("authDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
}
