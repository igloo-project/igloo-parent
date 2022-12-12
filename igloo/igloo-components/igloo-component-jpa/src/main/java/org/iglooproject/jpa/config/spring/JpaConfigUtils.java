package org.iglooproject.jpa.config.spring;

import static com.google.common.base.Strings.emptyToNull;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.NamingException;
import javax.persistence.SharedCacheMode;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.apache.lucene.analysis.Analyzer;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.TypeContributorList;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;
import org.hibernate.loader.BatchFetchStyle;
import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.config.spring.provider.IDatabaseConnectionConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.IDatabaseConnectionJndiConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.IDatabaseConnectionPoolConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.IJpaConfigurationProvider;
import org.iglooproject.jpa.config.spring.provider.JpaPackageScanProvider;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.hibernate.jpa.PerTableSequenceStrategyProvider;
import org.iglooproject.jpa.hibernate.model.naming.PostgreSQLPhysicalNamingStrategyImpl;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;

import igloo.hibernateconfig.api.HibernateCacheRegionFactory;
import igloo.hibernateconfig.api.HibernateSearchBackend;
import igloo.hibernateconfig.api.HibernateSearchConfigurator;
import igloo.hibernateconfig.api.IJpaPropertiesProvider;

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
		properties.setProperty(AvailableSettings.DEFAULT_SCHEMA, configuration.getDefaultSchema());
		properties.setProperty(AvailableSettings.DIALECT, configuration.getDialect().getName());
		properties.setProperty(AvailableSettings.HBM2DDL_AUTO, configuration.getHbm2Ddl());
		properties.setProperty(AvailableSettings.SHOW_SQL, Boolean.FALSE.toString());
		properties.setProperty(AvailableSettings.FORMAT_SQL, Boolean.FALSE.toString());
		properties.setProperty(AvailableSettings.GENERATE_STATISTICS, Boolean.FALSE.toString());
		properties.setProperty(AvailableSettings.USE_REFLECTION_OPTIMIZER, Boolean.TRUE.toString());
		properties.setProperty(AvailableSettings.CREATE_EMPTY_COMPOSITES_ENABLED, Boolean.toString(configuration.isCreateEmptyCompositesEnabled()));
		properties.setProperty(AvailableSettings.XML_MAPPING_ENABLED, Boolean.toString(configuration.isXmlMappingEnabled()));
		
		properties.setProperty(AvailableSettings.JPAQL_STRICT_COMPLIANCE, Boolean.TRUE.toString());
		
		Integer defaultBatchSize = configuration.getDefaultBatchSize();
		if (defaultBatchSize != null) {
			properties.setProperty(AvailableSettings.DEFAULT_BATCH_FETCH_SIZE, Integer.toString(defaultBatchSize));
			properties.setProperty(AvailableSettings.BATCH_FETCH_STYLE, BatchFetchStyle.PADDED.name());
		}
		
		String hibernateHbm2DdlImportFiles = configuration.getHbm2DdlImportFiles();
		if (StringUtils.hasText(hibernateHbm2DdlImportFiles)) {
			properties.setProperty(AvailableSettings.HBM2DDL_IMPORT_FILES, hibernateHbm2DdlImportFiles);
		}

		boolean queryCacheEnabled = configuration.isQueryCacheEnabled();
		HibernateCacheRegionFactory cacheRegionFactory = configuration.getEhCacheRegionFactory();
		if (!cacheRegionFactory.isDisabled()) {
			properties.setProperty(AvailableSettings.CACHE_REGION_FACTORY, cacheRegionFactory.getRegionFactoryAlias());
			if (cacheRegionFactory.isEhcache2()) {
				// org.hibernate.cache.ehcache.ConfigSettings.EHCACHE_CONFIGURATION_RESOURCE_NAME
				properties.setProperty("net.sf.ehcache.configurationResourceName", configuration.getEhCacheConfiguration());
			} else if (cacheRegionFactory.isJcache()) {
				properties.setProperty("hibernate.javax.cache.missing_cache_strategy", "create-warn");
				properties.setProperty("hibernate.javax.cache.provider", cacheRegionFactory.getJcacheProvider());
				properties.setProperty("hibernate.javax.cache.uri", configuration.getJcacheConfiguration());
			}
			
			properties.setProperty(AvailableSettings.JPA_SHARED_CACHE_MODE, SharedCacheMode.ENABLE_SELECTIVE.name());
			properties.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, Boolean.TRUE.toString());
			if (queryCacheEnabled) {
				properties.setProperty(AvailableSettings.USE_QUERY_CACHE, Boolean.TRUE.toString());
			} else {
				properties.setProperty(AvailableSettings.USE_QUERY_CACHE, Boolean.FALSE.toString());
			}
		} else {
			if (queryCacheEnabled) {
				throw new IllegalArgumentException("Could not enable query cache without EhCache configuration");
			}
			properties.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, Boolean.FALSE.toString());
			properties.setProperty(AvailableSettings.USE_QUERY_CACHE, Boolean.FALSE.toString());
		}

		String hibernateSearchIndexBase = configuration.getHibernateSearchIndexBase();
		HibernateSearchBackend backend;
		if (configuration.isHibernateSearchElasticSearchEnabled()) {
			backend = HibernateSearchBackend.ELASTICSEARCH;
		} else if (StringUtils.hasText(hibernateSearchIndexBase)) {
			backend = HibernateSearchBackend.LUCENE;
		} else {
			backend = HibernateSearchBackend.DISABLED;
		}
		HibernateSearchConfigurator.load(backend).configureHibernateSearch(configuration, properties);
		
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
		
		Class<? extends IdentifierGeneratorStrategyProvider> identifierGeneratorStategyProvider =
				configuration.getIdentifierGeneratorStrategyProvider();
		// Using PerTableSequence by default
		String identifierGeneratorStrategyProviderName = identifierGeneratorStategyProvider != null ?
				identifierGeneratorStategyProvider.getName() : PerTableSequenceStrategyProvider.class.getName();
		properties.setProperty(AvailableSettings.IDENTIFIER_GENERATOR_STRATEGY_PROVIDER,
				identifierGeneratorStrategyProviderName);
		
		Class<? extends ImplicitNamingStrategy> implicitNamingStrategy = configuration.getImplicitNamingStrategy();
		if (implicitNamingStrategy != null) {
			properties.setProperty(AvailableSettings.IMPLICIT_NAMING_STRATEGY, implicitNamingStrategy.getName());
		} else {
			throw new IllegalStateException(AvailableSettings.IMPLICIT_NAMING_STRATEGY + " may not be null: sensible values are "
					+ ImplicitNamingStrategyJpaCompliantImpl.class.getName() + " for Igloo <= 0.7 or "
					+ ImplicitNamingStrategyComponentPathImpl.class.getName() + " for Igloo >= 0.8");
		}
		
		Class<? extends PhysicalNamingStrategy> physicalNamingStrategy = configuration.getPhysicalNamingStrategy();
		if (physicalNamingStrategy != null) {
			properties.setProperty(AvailableSettings.PHYSICAL_NAMING_STRATEGY, physicalNamingStrategy.getName());
		} else {
			throw new IllegalStateException(AvailableSettings.PHYSICAL_NAMING_STRATEGY + " may not be null: sensible values are "
					+ PhysicalNamingStrategyStandardImpl.class.getName() + " for no filtering of the name "
					+ PostgreSQLPhysicalNamingStrategyImpl.class.getName() + " to truncate the name to conform with PostgreSQL identifier max length");
		}
		
		Boolean isNewGeneratorMappingsEnabled = configuration.isNewGeneratorMappingsEnabled();
		if (isNewGeneratorMappingsEnabled != null) {
			properties.setProperty(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, isNewGeneratorMappingsEnabled.toString());
		}
		
		properties.put(EntityManagerFactoryBuilderImpl.INTEGRATOR_PROVIDER, configuration.getIntegratorProvider());
		if (configuration.getTypeContributors() != null) {
			properties.put(
				EntityManagerFactoryBuilderImpl.TYPE_CONTRIBUTORS,
				new TypeContributorListImpl(configuration.getTypeContributors())
			);
		}
		
		// Override properties
		properties.putAll(configuration.getDefaultExtraProperties());
		properties.putAll(configuration.getExtraProperties());
		
		return properties;
	}

	public static class TypeContributorListImpl implements TypeContributorList {
		private final List<TypeContributor> contributors;
		public TypeContributorListImpl(List<TypeContributor> contributors) {
			this.contributors = ImmutableList.copyOf(contributors);
		}
		@Override
		public List<TypeContributor> getTypeContributors() {
			return contributors;
		}
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

	public static DataSource dataSource(IDatabaseConnectionConfigurationProvider configurationProvider) {
		if (configurationProvider instanceof IDatabaseConnectionJndiConfigurationProvider) {
			String jndiName = ((IDatabaseConnectionJndiConfigurationProvider) configurationProvider).getJndiName();
			JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
			bean.setJndiName(jndiName);
			bean.setExpectedType(DataSource.class);
			try {
				bean.afterPropertiesSet();
			} catch (IllegalArgumentException | NamingException e) {
				throw new IllegalStateException(String.format("Error during jndi lookup for %s resource",
						jndiName), e);
			}
			return (DataSource) bean.getObject();
		} else if (configurationProvider instanceof IDatabaseConnectionPoolConfigurationProvider) {
			IDatabaseConnectionPoolConfigurationProvider provider =
					(IDatabaseConnectionPoolConfigurationProvider) configurationProvider;
			HikariDataSource dataSource = new HikariDataSource();
			
			dataSource.setDriverClassName(configurationProvider.getDriverClass().getName());
			dataSource.setJdbcUrl(provider.getUrl());
			dataSource.setUsername(provider.getUser());
			dataSource.setPassword(provider.getPassword());
			dataSource.addDataSourceProperty("user", provider.getUser());
			dataSource.addDataSourceProperty("password", provider.getPassword());
			dataSource.setMinimumIdle(provider.getMinIdle());
			dataSource.setMaximumPoolSize(provider.getMaxPoolSize());
			dataSource.setConnectionTestQuery(provider.getValidationQuery());
			dataSource.setConnectionInitSql(emptyToNull(provider.getInitSql()));
			
			return dataSource;
		} else {
			throw new IllegalStateException(String.format("JDBC pool : %s type not handled",
					configurationProvider.getClass().getName()));
		}
	}

	private static String[] getPackagesToScan(List<JpaPackageScanProvider> jpaPackageScanProviders) {
		Set<String> packagesToScan = new HashSet<>();
		for (JpaPackageScanProvider jpaPackageScanProvider : jpaPackageScanProviders) {
			for (Package packageToScan : jpaPackageScanProvider.getPackages()) {
				packagesToScan.add(packageToScan.getName());
			}
		}
		return packagesToScan.toArray(new String[packagesToScan.size()]);
	}

}
