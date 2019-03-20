package org.igloo.spring.autoconfigure.jpa;


import static org.iglooproject.jpa.property.JpaPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.lucene.search.BooleanQuery;
import org.igloo.spring.autoconfigure.JpaSpringAutoConfigureTestPackage;
import org.igloo.spring.autoconfigure.flyway.IglooFlywayAutoConfiguration;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.jpa.batch.CoreJpaBatchPackage;
import org.iglooproject.jpa.business.generic.CoreJpaBusinessGenericPackage;
import org.iglooproject.jpa.config.spring.DefaultJpaConfig;
import org.iglooproject.jpa.config.spring.JpaApplicationPropertyRegistryConfig;
import org.iglooproject.jpa.config.spring.JpaConfigUtils;
import org.iglooproject.jpa.config.spring.provider.DefaultJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.hibernate.integrator.spi.MetadataRegistryIntegrator;
import org.iglooproject.jpa.util.CoreJpaUtilPackage;
import org.iglooproject.spring.property.exception.PropertyServiceIncompleteRegistrationException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ComponentScan(
	basePackageClasses = {
			CoreJpaBatchPackage.class,
			CoreJpaBusinessGenericPackage.class,
			CoreJpaUtilPackage.class
	},
	excludeFilters = @Filter(Configuration.class)
)
@Import({ DefaultJpaConfig.class })
@AutoConfigureAfter({ IglooPropertyAutoConfiguration.class, IglooFlywayAutoConfiguration.class })
@PropertySource({ IglooJpaAutoConfiguration.PROPERTIES_COMPONENT_JPA, ConfigurationPropertiesUrlConstants.JPA_COMMON })
public class IglooJpaAutoConfiguration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IglooJpaAutoConfiguration.class);

	public static final String PROPERTIES_COMPONENT_JPA = "classpath:igloo-component-jpa.properties";

	@Autowired(required = false)
	private IPropertyService propertyService;
	
	@PostConstruct
	public void init() {
		try {
			BooleanQuery.setMaxClauseCount(propertyService.get(LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT));
		} catch (PropertyServiceIncompleteRegistrationException e) {
			LOGGER.warn(String.format("Property boolean query max clause count doesn't exists, value is set to %d",
					BooleanQuery.getMaxClauseCount()));
		} catch  (NullPointerException e) {
			LOGGER.warn(String.format("Property service is null, boolean query max clause count value is set to %d",
					BooleanQuery.getMaxClauseCount()));
		}
	}

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
	 * Déclaration explicite de close comme destroyMethod (Spring doit la prendre en compte auto-magiquement même
	 * si non configurée).
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource(DefaultJpaConfig defaultJpaConfig) {
		return JpaConfigUtils.dataSource(defaultJpaConfig.defaultDatabaseConnectionPoolConfigurationProvider(null));
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

	@Bean
	@DependsOn("databaseInitialization")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DefaultJpaConfigurationProvider defaultJpaCoreConfigurationProvider) {
		return JpaConfigUtils.entityManagerFactory(defaultJpaCoreConfigurationProvider);
	}

	@Bean
	public JpaPackageScanProvider applicationJpaPackageScanProvider() {
		return new JpaPackageScanProvider(JpaSpringAutoConfigureTestPackage.class.getPackage());
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