package org.iglooproject.jpa.config.spring;

import static org.iglooproject.jpa.property.JpaPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.lucene.search.BooleanQuery;
import org.flywaydb.core.Flyway;
import org.iglooproject.flyway.FlywayInitializer;
import org.iglooproject.flyway.IFlywayConfiguration;
import org.iglooproject.jpa.batch.CoreJpaBatchPackage;
import org.iglooproject.jpa.business.generic.CoreJpaBusinessGenericPackage;
import org.iglooproject.jpa.config.spring.provider.IDatabaseConnectionConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.hibernate.integrator.spi.MetadataRegistryIntegrator;
import org.iglooproject.jpa.more.config.util.FlywayConfiguration;
import org.iglooproject.jpa.property.FlywayPropertyIds;
import org.iglooproject.jpa.search.CoreJpaSearchPackage;
import org.iglooproject.jpa.util.CoreJpaUtilPackage;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * L'implémentation de cette classe doit être annotée {@link EnableAspectJAutoProxy}
 */
@ComponentScan(
	basePackageClasses = {
		CoreJpaBatchPackage.class,
		CoreJpaBusinessGenericPackage.class,
		CoreJpaSearchPackage.class,
		CoreJpaUtilPackage.class
	},
	excludeFilters = @Filter(Configuration.class)
)
@Import(FlywayPropertyRegistryConfig.class)
public abstract class AbstractJpaConfig {

	@Autowired
	protected IDatabaseConnectionConfigurationProvider databaseConfigurationProvider;

	@Autowired
	protected IJpaConfigurationProvider jpaConfigurationProvider;
	
	@Autowired
	private IPropertyService propertyService;
	
	@PostConstruct
	public void init() {
		BooleanQuery.setMaxClauseCount(propertyService.get(LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT));
	}

	@Bean
	public MetadataRegistryIntegrator metdataRegistryIntegrator() {
		return new MetadataRegistryIntegrator();
	}

	@Bean(initMethod = "migrate", value = { "flyway", "databaseInitialization" })
	@Profile("flyway")
	public Flyway flyway(DataSource dataSource, IFlywayConfiguration flywayConfiguration,
		IPropertyService propertyService, ConfigurableApplicationContext applicationContext) {
		Map<String, String> placeholders = new HashMap<>();
		for (String property : propertyService.get(FlywayPropertyIds.FLYWAY_PLACEHOLDERS_PROPERTIES)) {
			placeholders.put(property, propertyService.get(FlywayPropertyIds.property(property)));
		}
		return FlywayInitializer.flyway(
				dataSource,
				flywayConfiguration,
				placeholders,
				applicationContext.getAutowireCapableBeanFactory()::autowireBean);
	}

	@Bean
	@Profile("flyway")
	public IFlywayConfiguration flywayConfiguration() {
		return new FlywayConfiguration();
	}

	/**
	 * Placeholder when flyway is not enabled
	 */
	@Bean(value = { "flyway", "databaseInitialization" })
	@Profile("!flyway")
	public Object notFlyway() {
		return new Object();
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
	public DataSource dataSource() {
		return JpaConfigUtils.dataSource(databaseConfigurationProvider);
	}

	@Bean
	@DependsOn("databaseInitialization")
	public abstract LocalContainerEntityManagerFactoryBean entityManagerFactory();

	@Bean
	public abstract JpaPackageScanProvider applicationJpaPackageScanProvider();

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

}
