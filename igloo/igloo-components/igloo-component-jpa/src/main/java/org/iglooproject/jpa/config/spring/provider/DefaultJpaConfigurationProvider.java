package org.iglooproject.jpa.config.spring.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.spi.PersistenceProvider;
import javax.sql.DataSource;

import org.apache.lucene.analysis.Analyzer;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class DefaultJpaConfigurationProvider implements IJpaConfigurationProvider {

	@Autowired
	private List<JpaPackageScanProvider> jpaPackageScanProviders;

	@Value("${db.dialect}")
	private Class<Dialect> dialect;

	@Value("${db.schema}")
	private String defaultSchema;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2Ddl;

	@Value("${hibernate.hbm2ddl.import_files}")
	private String hbm2DdlImportFiles;

	@Value("${hibernate.defaultBatchSize}")
	private Integer defaultBatchSize;

	@Value("${lucene.index.path}")
	private String hibernateSearchIndexBase;
	
	@Value("${lucene.index.inRam:false}")
	private boolean isHibernateSearchIndexInRam;
	
	@Value("${hibernate.search.analyzer:}") // Defaults to null
	private Class<? extends Analyzer> hibernateSearchDefaultAnalyzer;
	
	@Value("${hibernate.search.indexing_strategy:}") // Defaults to an empty string
	private String hibernateSearchIndexingStrategy;

	@Value("#{dataSource}")
	private DataSource dataSource;

	@Value("${hibernate.ehCache.configurationLocation}")
	private String ehCacheConfiguration;

	@Value("${hibernate.ehCache.singleton}")
	private boolean ehCacheSingleton;
	
	@Value("${hibernate.ehCache.regionFactory:}")
	private Class<? extends RegionFactory> ehCacheRegionFactory;

	@Value("${hibernate.queryCache.enabled}")
	private boolean queryCacheEnabled;

	@Autowired(required=false)
	private PersistenceProvider persistenceProvider;

	@Value("${javax.persistence.validation.mode}")
	private String validationMode;
	
	@Value("${hibernate.implicit_naming_strategy}")
	private Class<ImplicitNamingStrategy> implicitNamingStrategy;

	@Value("${hibernate.physical_naming_strategy}")
	private Class<PhysicalNamingStrategy> physicalNamingStrategy;
	
	@Value("${hibernate.identifier_generator_strategy_provider:}") // Defaults to null
	private Class<IdentifierGeneratorStrategyProvider> identifierGeneratorStrategyProvider;
	
	@Value("${hibernate.id.new_generator_mappings}")
	private Boolean isNewGeneratorMappingsEnabled;

	@Value("${hibernate.create_empty_composites.enabled}")
	private boolean createEmptyCompositesEnabled;

	@Value("${hibernate.search.elasticsearch.enabled:false}")
	private boolean isHibernateSearchElasticSearchEnabled;
	
	@Value("${hibernate.search.default.elasticsearch.host}")
	private String elasticSearchHost;

	@Value("${hibernate.search.default.elasticsearch.index_schema_management_strategy}")
	private String elasticSearchIndexSchemaManagementStrategy;

	/**
	 * If set to true, dom4j and jaxb dependencies must be provided on classpath
	 */
	@Value("${hibernate.xml_mapping_enabled:false}")
	private boolean xmlMappingEnabled;
	
	@Resource(name = "hibernateDefaultExtraProperties")
	private Properties defaultExtraProperties;

	@Resource(name = "hibernateExtraProperties")
	private Properties extraProperties;

	@Autowired(required = false)
	private List<Integrator> integrators;

	@Autowired(required = false)
	private List<TypeContributor> typeContributors;

	@Override
	public List<JpaPackageScanProvider> getJpaPackageScanProviders() {
		return jpaPackageScanProviders;
	}

	@Override
	public Class<Dialect> getDialect() {
		return dialect;
	}

	@Override
	public String getHbm2Ddl() {
		return hbm2Ddl;
	}

	@Override
	public String getHbm2DdlImportFiles() {
		return hbm2DdlImportFiles;
	}

	@Override
	public Integer getDefaultBatchSize() {
		return defaultBatchSize;
	}

	@Override
	public String getHibernateSearchIndexBase() {
		return hibernateSearchIndexBase;
	}
	
	@Override
	public boolean isHibernateSearchIndexInRam() {
		return isHibernateSearchIndexInRam;
	}

	@Override
	public Class<? extends Analyzer> getHibernateSearchDefaultAnalyzer() {
		return hibernateSearchDefaultAnalyzer;
	}
	
	@Override
	public String getHibernateSearchIndexingStrategy() {
		return hibernateSearchIndexingStrategy;
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public String getEhCacheConfiguration() {
		return ehCacheConfiguration;
	}

	@Override
	public boolean isEhCacheSingleton() {
		return ehCacheSingleton;
	}
	
	@Override
	public Class<? extends RegionFactory> getEhCacheRegionFactory() {
		return ehCacheRegionFactory;
	}

	@Override
	public boolean isQueryCacheEnabled() {
		return queryCacheEnabled;
	}

	@Override
	public PersistenceProvider getPersistenceProvider() {
		return persistenceProvider;
	}

	@Override
	public String getValidationMode() {
		return validationMode;
	}

	@Override
	public Class<? extends ImplicitNamingStrategy> getImplicitNamingStrategy() {
		return implicitNamingStrategy;
	}

	@Override
	public Class<? extends PhysicalNamingStrategy> getPhysicalNamingStrategy() {
		return physicalNamingStrategy;
	}

	@Override
	public Class<? extends IdentifierGeneratorStrategyProvider> getIdentifierGeneratorStrategyProvider() {
		return identifierGeneratorStrategyProvider;
	}

	@Override
	public Boolean isNewGeneratorMappingsEnabled() {
		return isNewGeneratorMappingsEnabled;
	}

	@Override
	public boolean isCreateEmptyCompositesEnabled() {
		return createEmptyCompositesEnabled;
	}

	@Override
	public boolean isXmlMappingEnabled() {
		return xmlMappingEnabled;
	}

	@Override
	public boolean isHibernateSearchElasticSearchEnabled() {
		return isHibernateSearchElasticSearchEnabled;
	}

	@Override
	public void setHibernateSearchElasticSearchEnabled(boolean isElasticSearchEnabled) {
		this.isHibernateSearchElasticSearchEnabled = isElasticSearchEnabled;
	}

	@Override
	public String getElasticSearchHost() {
		return elasticSearchHost;
	}

	@Override
	public void setElasticSearchHost(String elasticSearchHost) {
		this.elasticSearchHost = elasticSearchHost;
	}

	@Override
	public String getElasticSearchIndexSchemaManagementStrategy() {
		return elasticSearchIndexSchemaManagementStrategy;
	}

	@Override
	public void setElasticSearchIndexSchemaManagementStrategy(String elasticSearchIndexSchemaManagementStrategy) {
		this.elasticSearchIndexSchemaManagementStrategy = elasticSearchIndexSchemaManagementStrategy;
	}

	@Override
	public String getDefaultSchema() {
		return defaultSchema;
	}

	@Override
	public Properties getDefaultExtraProperties() {
		return defaultExtraProperties;
	}

	@Override
	public void setDefaultExtraProperties(Properties defaultExtraProperties) {
		this.defaultExtraProperties = defaultExtraProperties;
	}

	@Override
	public Properties getExtraProperties() {
		return extraProperties;
	}

	@Override
	public void setExtraProperties(Properties extraProperties) {
		this.extraProperties = extraProperties;
	}

	@Override
	public IntegratorProvider getIntegratorProvider() {
		// do a snapshot; this method is called once during context startup.
		final List<Integrator> integratorsSnapshot = new ArrayList<>();
		if (integrators != null) {
			integratorsSnapshot.addAll(integrators);
		}
		return () -> integratorsSnapshot;
	}

	@Override
	public List<TypeContributor> getTypeContributors() {
		return typeContributors;
	}

}
