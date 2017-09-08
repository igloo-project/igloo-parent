package fr.openwide.core.jpa.config.spring;

import static com.google.common.base.Strings.emptyToNull;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.SharedCacheMode;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.apache.lucene.analysis.Analyzer;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Environment;
import org.hibernate.loader.BatchFetchStyle;
import org.hibernate.search.elasticsearch.cfg.ElasticsearchEnvironment;
import org.hibernate.search.store.impl.FSDirectoryProvider;
import org.hibernate.search.store.impl.RAMDirectoryProvider;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.config.spring.provider.IDatabaseConnectionPoolConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.IJpaConfigurationProvider;
import fr.openwide.core.jpa.config.spring.provider.IJpaPropertiesProvider;
import fr.openwide.core.jpa.config.spring.provider.JpaPackageScanProvider;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.hibernate.analyzers.CoreElasticSearchAnalyzersDefinitionProvider;
import fr.openwide.core.jpa.hibernate.analyzers.CoreLuceneAnalyzersDefinitionProvider;
import fr.openwide.core.jpa.hibernate.analyzers.CoreLuceneClientAnalyzersDefinitionProvider;
import fr.openwide.core.jpa.hibernate.jpa.PerTableSequenceStrategyProvider;
import fr.openwide.core.jpa.hibernate.model.naming.PostgreSQLPhysicalNamingStrategyImpl;

public final class JpaConfigUtils {

	private JpaConfigUtils() {}

	/**
	 * Construit un {@link LocalContainerEntityManagerFactoryBean} à partir d'un ensemble d'options usuelles.
	 */
	public static LocalContainerEntityManagerFactoryBean entityManagerFactory(IJpaConfigurationProvider provider) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		
		entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		entityManagerFactoryBean.setJpaProperties(getJpaProperties(provider));
		entityManagerFactoryBean.setDataSource(provider.getDataSource());
		entityManagerFactoryBean.setPackagesToScan(getPackagesToScan(provider.getJpaPackageScanProviders()));
		
		PersistenceProvider persistenceProvider = provider.getPersistenceProvider();
		if (persistenceProvider != null) {
			entityManagerFactoryBean.setPersistenceProvider(persistenceProvider);
		}
		
		return entityManagerFactoryBean;
	}

	public static Properties getJpaProperties(IJpaPropertiesProvider configuration) {
		Properties properties = new Properties();
		properties.setProperty(Environment.DEFAULT_SCHEMA, configuration.getDefaultSchema());
		properties.setProperty(Environment.DIALECT, configuration.getDialect().getName());
		properties.setProperty(Environment.HBM2DDL_AUTO, configuration.getHbm2Ddl());
		properties.setProperty(Environment.SHOW_SQL, Boolean.FALSE.toString());
		properties.setProperty(Environment.FORMAT_SQL, Boolean.FALSE.toString());
		properties.setProperty(Environment.GENERATE_STATISTICS, Boolean.FALSE.toString());
		properties.setProperty(Environment.USE_REFLECTION_OPTIMIZER, Boolean.TRUE.toString());
		properties.setProperty(Environment.CREATE_EMPTY_COMPOSITES_ENABLED,
				Boolean.valueOf(configuration.isCreateEmptyCompositesEnabled()).toString());
		Integer defaultBatchSize = configuration.getDefaultBatchSize();
		if (defaultBatchSize != null) {
			properties.setProperty(Environment.DEFAULT_BATCH_FETCH_SIZE, Integer.toString(defaultBatchSize));
			properties.setProperty(Environment.BATCH_FETCH_STYLE, BatchFetchStyle.PADDED.name());
		}
		
		String hibernateHbm2DdlImportFiles = configuration.getHbm2DdlImportFiles();
		if (StringUtils.hasText(hibernateHbm2DdlImportFiles)) {
			properties.setProperty(Environment.HBM2DDL_IMPORT_FILES, hibernateHbm2DdlImportFiles);
		}

		String ehCacheConfiguration = configuration.getEhCacheConfiguration();
		boolean singletonCache = configuration.isEhCacheSingleton();
		boolean queryCacheEnabled = configuration.isQueryCacheEnabled();
		if (StringUtils.hasText(ehCacheConfiguration)) {
			if (configuration.getEhCacheRegionFactory() != null) {
				properties.setProperty(Environment.CACHE_REGION_FACTORY, configuration.getEhCacheRegionFactory().getName());
			} else {
				if (singletonCache) {
					properties.setProperty(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName());
				} else {
					properties.setProperty(Environment.CACHE_REGION_FACTORY, EhCacheRegionFactory.class.getName());
				}
			}
			properties.setProperty(AvailableSettings.JPA_SHARED_CACHE_MODE, SharedCacheMode.ENABLE_SELECTIVE.name());
			properties.setProperty(EhCacheRegionFactory.NET_SF_EHCACHE_CONFIGURATION_RESOURCE_NAME, ehCacheConfiguration);
			properties.setProperty(Environment.USE_SECOND_LEVEL_CACHE, Boolean.TRUE.toString());
			if (queryCacheEnabled) {
				properties.setProperty(Environment.USE_QUERY_CACHE, Boolean.TRUE.toString());
			} else {
				properties.setProperty(Environment.USE_QUERY_CACHE, Boolean.FALSE.toString());
			}
		} else {
			if (queryCacheEnabled) {
				throw new IllegalArgumentException("Could not enable query cache without EhCache configuration");
			}
			properties.setProperty(Environment.USE_SECOND_LEVEL_CACHE, Boolean.FALSE.toString());
			properties.setProperty(Environment.USE_QUERY_CACHE, Boolean.FALSE.toString());
		}

		String hibernateSearchIndexBase = configuration.getHibernateSearchIndexBase();
		
		if (configuration.isHibernateSearchElasticSearchEnabled()) {
			properties.setProperty(ElasticsearchEnvironment.ANALYZER_DEFINITION_PROVIDER, CoreElasticSearchAnalyzersDefinitionProvider.class.getName());
			properties.setProperty(org.hibernate.search.cfg.Environment.ANALYZER_DEFINITION_PROVIDER, CoreLuceneClientAnalyzersDefinitionProvider.class.getName());
			properties.setProperty("hibernate.search.default.indexmanager", "elasticsearch");
			properties.setProperty("hibernate.search.default.elasticsearch.host", configuration.getElasticSearchHost());
			properties.setProperty("hibernate.search.default.elasticsearch.index_schema_management_strategy", configuration.getElasticSearchIndexSchemaManagementStrategy());
		} else if (StringUtils.hasText(hibernateSearchIndexBase)) {
			properties.setProperty(org.hibernate.search.cfg.Environment.ANALYZER_DEFINITION_PROVIDER, CoreLuceneAnalyzersDefinitionProvider.class.getName());
			if (configuration.isHibernateSearchIndexInRam()) {
				properties.setProperty("hibernate.search.default.directory_provider", RAMDirectoryProvider.class.getName());
			} else {
				properties.setProperty("hibernate.search.default.directory_provider", FSDirectoryProvider.class.getName());
				properties.setProperty("hibernate.search.default.locking_strategy", "native");
			}
			
			properties.setProperty("hibernate.search.default.indexBase", hibernateSearchIndexBase);
			properties.setProperty("hibernate.search.default.exclusive_index_use", Boolean.TRUE.toString());
			properties.setProperty(org.hibernate.search.cfg.Environment.LUCENE_MATCH_VERSION,
					org.hibernate.search.cfg.Environment.DEFAULT_LUCENE_MATCH_VERSION.toString());
		} else {
			properties.setProperty("hibernate.search.autoregister_listeners", Boolean.FALSE.toString());
		}
		
		Class<? extends Analyzer> hibernateSearchDefaultAnalyzer = configuration.getHibernateSearchDefaultAnalyzer();
		if (hibernateSearchDefaultAnalyzer != null) {
			properties.setProperty(org.hibernate.search.cfg.Environment.ANALYZER_CLASS, hibernateSearchDefaultAnalyzer.getName());
		}
		
		String hibernateSearchIndexingStrategy = configuration.getHibernateSearchIndexingStrategy();
		if (StringUtils.hasText(hibernateSearchIndexingStrategy)) {
			properties.setProperty(org.hibernate.search.cfg.Environment.INDEXING_STRATEGY, hibernateSearchIndexingStrategy);
		}

		String validationMode = configuration.getValidationMode();
		if (StringUtils.hasText(validationMode)) {
			properties.setProperty(AvailableSettings.JPA_VALIDATION_MODE, validationMode);
		}
		
		// custom generator strategy provider that handles one sequence by entity
		properties.setProperty(org.hibernate.jpa.AvailableSettings.IDENTIFIER_GENERATOR_STRATEGY_PROVIDER,
				PerTableSequenceStrategyProvider.class.getName());
		
		Class<? extends ImplicitNamingStrategy> implicitNamingStrategy = configuration.getImplicitNamingStrategy();
		if (implicitNamingStrategy != null) {
			properties.setProperty(Environment.IMPLICIT_NAMING_STRATEGY, implicitNamingStrategy.getName());
		} else {
			throw new IllegalStateException(Environment.IMPLICIT_NAMING_STRATEGY + " may not be null: sensible values are "
					+ ImplicitNamingStrategyJpaCompliantImpl.class.getName() + " for OWSI-Core <= 0.7 or "
					+ ImplicitNamingStrategyComponentPathImpl.class.getName() + " for OWSI-Core >= 0.8");
		}
		
		Class<? extends PhysicalNamingStrategy> physicalNamingStrategy = configuration.getPhysicalNamingStrategy();
		if (physicalNamingStrategy != null) {
			properties.setProperty(Environment.PHYSICAL_NAMING_STRATEGY, physicalNamingStrategy.getName());
		} else {
			throw new IllegalStateException(Environment.PHYSICAL_NAMING_STRATEGY + " may not be null: sensible values are "
					+ PhysicalNamingStrategyStandardImpl.class.getName() + " for no filtering of the name "
					+ PostgreSQLPhysicalNamingStrategyImpl.class.getName() + " to truncate the name to conform with PostgreSQL identifier max length");
		}
		
		Boolean isNewGeneratorMappingsEnabled = configuration.isNewGeneratorMappingsEnabled();
		if (isNewGeneratorMappingsEnabled != null) {
			properties.setProperty(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, isNewGeneratorMappingsEnabled.toString());
		}
		
		// Override properties
		properties.putAll(configuration.getDefaultExtraProperties());
		properties.putAll(configuration.getExtraProperties());
		
		return properties;
	}

	public static Advisor defaultTransactionAdvisor(PlatformTransactionManager transactionManager) {
		return defaultTransactionAdvisor(transactionManager, Lists.<Class<? extends Exception>>newArrayList());
	}

	public static Advisor defaultTransactionAdvisor(PlatformTransactionManager transactionManager,
			List<Class<? extends Exception>> additionalRollbackRuleExceptions) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		
		advisor.setExpression("this(" + ITransactionalAspectAwareService.class.getName() + ")");
		advisor.setAdvice(defaultTransactionInterceptor(transactionManager, additionalRollbackRuleExceptions));
		
		return advisor;
		
	}

	/**
	 * Construit un transactionInterceptor avec une configuration par défaut.
	 */
	public static TransactionInterceptor defaultTransactionInterceptor(PlatformTransactionManager transactionManager,
			List<Class<? extends Exception>> additionalRollbackRuleExceptions) {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		Properties transactionAttributes = new Properties();
		
		List<RollbackRuleAttribute> rollbackRules = Lists.newArrayList();
		rollbackRules.add(new RollbackRuleAttribute(ServiceException.class));
		// TODO voir si on ajoute SecurityServiceException.class en fonction de ce que ça donne sur le Wombat
		// ou voir si on ne la dégage pas carrément en fait...
		
		for (Class<? extends Exception> clazz : additionalRollbackRuleExceptions) {
			rollbackRules.add(new RollbackRuleAttribute(clazz));
		}
		
		DefaultTransactionAttribute readOnlyTransactionAttributes =
				new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED);
		readOnlyTransactionAttributes.setReadOnly(true);
		
		RuleBasedTransactionAttribute writeTransactionAttributes =
				new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		
		String readOnlyTransactionAttributesDefinition = readOnlyTransactionAttributes.toString();
		String writeTransactionAttributesDefinition = writeTransactionAttributes.toString();
		// read-only
		transactionAttributes.setProperty("is*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("has*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("search*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("find*", readOnlyTransactionAttributesDefinition);
		transactionAttributes.setProperty("count*", readOnlyTransactionAttributesDefinition);
		// write et rollback-rule
		transactionAttributes.setProperty("*", writeTransactionAttributesDefinition);
		
		transactionInterceptor.setTransactionAttributes(transactionAttributes);
		transactionInterceptor.setTransactionManager(transactionManager);
		return transactionInterceptor;
	}

	public static DataSource dataSource(IDatabaseConnectionPoolConfigurationProvider configurationProvider) {
		HikariDataSource dataSource = new HikariDataSource();
		
		dataSource.setDriverClassName(configurationProvider.getDriverClass().getName());
		dataSource.setJdbcUrl(configurationProvider.getUrl());
		dataSource.setUsername(configurationProvider.getUser());
		dataSource.setPassword(configurationProvider.getPassword());
		dataSource.addDataSourceProperty("user", configurationProvider.getUser());
		dataSource.addDataSourceProperty("password", configurationProvider.getPassword());
		dataSource.setMinimumIdle(configurationProvider.getMinIdle());
		dataSource.setMaximumPoolSize(configurationProvider.getMaxPoolSize());
		dataSource.setConnectionTestQuery(configurationProvider.getValidationQuery());
		dataSource.setConnectionInitSql(emptyToNull(configurationProvider.getInitSql()));
		
		return dataSource;
	}

	private static String[] getPackagesToScan(List<JpaPackageScanProvider> jpaPackageScanProviders) {
		Set<String> packagesToScan = new HashSet<String>();
		for (JpaPackageScanProvider jpaPackageScanProvider : jpaPackageScanProviders) {
			for (Package packageToScan : jpaPackageScanProvider.getPackages()) {
				packagesToScan.add(packageToScan.getName());
			}
		}
		return packagesToScan.toArray(new String[packagesToScan.size()]);
	}

}
