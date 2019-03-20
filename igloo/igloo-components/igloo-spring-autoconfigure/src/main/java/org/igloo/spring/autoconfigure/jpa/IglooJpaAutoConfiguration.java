package org.igloo.spring.autoconfigure.jpa;


import static org.iglooproject.jpa.property.JpaPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.BooleanQuery;
import org.flywaydb.core.Flyway;
import org.igloo.spring.autoconfigure.JpaSpringAutoConfigureTestPackage;
import org.iglooproject.jpa.batch.CoreJpaBatchPackage;
import org.iglooproject.jpa.business.generic.CoreJpaBusinessGenericPackage;
import org.iglooproject.jpa.config.spring.DefaultJpaConfig;
import org.iglooproject.jpa.config.spring.FlywayPropertyRegistryConfig;
import org.iglooproject.jpa.config.spring.JpaConfigUtils;
import org.iglooproject.jpa.config.spring.provider.DefaultJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.hibernate.integrator.spi.MetadataRegistryIntegrator;
import org.iglooproject.jpa.more.config.util.FlywayConfiguration;
import org.iglooproject.jpa.more.config.util.FlywaySpring;
import org.iglooproject.jpa.property.FlywayPropertyIds;
import org.iglooproject.jpa.search.CoreJpaSearchPackage;
import org.iglooproject.jpa.util.CoreJpaUtilPackage;
import org.iglooproject.spring.config.CorePropertyPlaceholderConfigurer;
import org.iglooproject.spring.property.dao.IImmutablePropertyDao;
import org.iglooproject.spring.property.dao.ImmutablePropertyDaoImpl;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.property.service.PropertyServiceImpl;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.google.common.collect.Maps;

@Configuration
@ComponentScan(
	basePackageClasses = {
			CoreJpaBatchPackage.class,
			CoreJpaBusinessGenericPackage.class,
			CoreJpaSearchPackage.class,
			CoreJpaUtilPackage.class
	},
	excludeFilters = @Filter(Configuration.class)
)
@Import({ FlywayPropertyRegistryConfig.class, DefaultJpaConfig.class })
@PropertySource({ IglooJpaAutoConfiguration.PROPERTIES_COMPONENT_JPA, ConfigurationPropertiesUrlConstants.JPA_COMMON })
public class IglooJpaAutoConfiguration {

	public static final String PROPERTIES_COMPONENT_JPA = "classpath:igloo-component-jpa.properties";
	
	@ConditionalOnMissingBean(CorePropertyPlaceholderConfigurer.class)
	@Bean
	public CorePropertyPlaceholderConfigurer corePropertyPlaceholderConfigurer() {
		return new CorePropertyPlaceholderConfigurer();
	}
	
	@ConditionalOnMissingBean(IImmutablePropertyDao.class)
	@Bean
	public IImmutablePropertyDao immutablePropertyDao() {
		return new ImmutablePropertyDaoImpl();
	}
	
	@ConditionalOnMissingBean(IPropertyService.class)
	@Bean
	public IPropertyService propertyService() {
		return new PropertyServiceImpl();
	}

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
	public Flyway flyway(DataSource dataSource, FlywayConfiguration flywayConfiguration,
			IPropertyService propertyService, ConfigurableApplicationContext applicationContext) {
		FlywaySpring flyway = new FlywaySpring();
		flyway.setApplicationContext(applicationContext);
		flyway.setDataSource(dataSource);
		flyway.setSchemas(flywayConfiguration.getSchemas()); 
		flyway.setTable(flywayConfiguration.getTable());
		flyway.setLocations(StringUtils.split(flywayConfiguration.getLocations(), ","));
		flyway.setBaselineOnMigrate(true);
		// difficult to handle this case for the moment; we ignore mismatching checksums
		// TODO allow developers to handle mismatches during their tests.
		flyway.setValidateOnMigrate(false);
		
		Map<String, String> placeholders = Maps.newHashMap();
		for (String property : propertyService.get(FlywayPropertyIds.FLYWAY_PLACEHOLDERS_PROPERTIES)) {
			placeholders.put(property, propertyService.get(FlywayPropertyIds.property(property)));
		}
		flyway.setPlaceholderReplacement(true);
		flyway.setPlaceholders(placeholders);
		
		return flyway;
	}

	@Bean
	@Profile("flyway")
	public FlywayConfiguration flywayConfiguration() {
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
	public DataSource dataSource(DefaultJpaConfig defaultJpaConfig) {
		return JpaConfigUtils.dataSource(defaultJpaConfig.defaultDatabaseConnectionPoolConfigurationProvider(null));
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

}