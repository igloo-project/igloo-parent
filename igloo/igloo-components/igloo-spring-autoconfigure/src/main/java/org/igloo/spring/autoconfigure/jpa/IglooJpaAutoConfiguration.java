package org.igloo.spring.autoconfigure.jpa;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.igloo.hibernate.hbm.MetadataRegistryIntegrator;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.batch.CoreJpaBatchPackage;
import org.iglooproject.jpa.business.generic.CoreJpaBusinessGenericPackage;
import org.iglooproject.jpa.config.spring.DefaultJpaConfig;
import org.iglooproject.jpa.config.spring.JpaApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.config.spring.JpaConfigUtils;
import org.iglooproject.jpa.config.spring.provider.IDatabaseConnectionConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.util.CoreJpaUtilPackage;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.jpa.disabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EntityManager.class })
@PropertySource(
	name = IglooPropertySourcePriority.COMPONENT,
	value = {
		IglooJpaAutoConfiguration.PROPERTIES_COMPONENT_JPA,
		"classpath:/configuration/jpa-common.properties"
	},
	encoding = "UTF-8"
)
@Import({ DefaultJpaConfig.class, IglooJpaCacheConfig.class })
@ComponentScan(
	basePackageClasses = {
		CoreJpaBatchPackage.class,
		CoreJpaBusinessGenericPackage.class,
		CoreJpaUtilPackage.class
	},
	excludeFilters = @Filter(Configuration.class)
)
public class IglooJpaAutoConfiguration {

	/**
	 * Generic configuration for common jpa/jdbc elements (dialect, db types, ...)
	 */
	public static final String PROPERTIES_COMPONENT_JPA = "classpath:igloo-component-jpa.properties";

	@Bean
	public MetadataRegistryIntegrator metdataRegistryIntegrator() {
		return new MetadataRegistryIntegrator();
	}

	@Bean(name = "hibernateDefaultExtraProperties")
	public PropertiesFactoryBean hibernateDefaultExtraProperties(@Value("${hibernate.defaultExtraPropertiesUrl}") Resource defaultExtraPropertiesUrl) {
		PropertiesFactoryBean f = new PropertiesFactoryBean();
		f.setIgnoreResourceNotFound(false);
		f.setFileEncoding("UTF-8");
		f.setLocations(defaultExtraPropertiesUrl);
		return f;
	}

	@Bean(name = "hibernateExtraProperties")
	public PropertiesFactoryBean hibernateExtraProperties(@Value("${hibernate.extraPropertiesUrl}") Resource extraPropertiesUrl) {
		PropertiesFactoryBean f = new PropertiesFactoryBean();
		f.setIgnoreResourceNotFound(true);
		f.setFileEncoding("UTF-8");
		f.setLocations(extraPropertiesUrl);
		return f;
	}

	/**
	 * Ensure that dataSource is correctly destroyed on context destruction with an explicit configuration.
	 * (Spring should automatically call close method on beans)
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource(IDatabaseConnectionConfigurationProvider configurationProvider) {
		return JpaConfigUtils.dataSource(configurationProvider);
	}

	/**
	 * Bean that materialized before-entity-manager creation step. Can be used by Spring configuration
	 * that needs to initialize something before entity manager creation (e.g. flyway).
	 */
	@Bean
	@ConditionalOnMissingBean(name = "databaseInitialization")
	public Object databaseInitialization() {
		return new Object();
	}

	/**
	 * We manage a before-entity-manager creation phase by introducing a databaseInitialization bean.
	 * Spring components that want to initialize some beans before entity manager creation must redefine this
	 * bean (databaseInitialization) and perform their initialization step before its creation.
	 * 
	 * @see #databaseInitialization()
	 */
	@Bean
	@ConditionalOnMissingBean
	@DependsOn("databaseInitialization")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(IJpaConfigurationProvider defaultJpaCoreConfigurationProvider) {
		return JpaConfigUtils.entityManagerFactory(defaultJpaCoreConfigurationProvider);
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	public Advisor transactionAdvisor(PlatformTransactionManager transactionManager) {
		return JpaConfigUtils.defaultTransactionAdvisor(transactionManager);
	}

	@Bean
	public JpaPackageScanProvider coreJpaPackageScanProvider() {
		return new JpaPackageScanProvider(CoreJpaBusinessGenericPackage.class.getPackage());
	}

	@Configuration
	@ConditionalOnBean(IPropertyService.class)
	@Import(JpaApplicationPropertyRegistryConfig.class)
	public static class JpaPropertyConfiguration {}

}
