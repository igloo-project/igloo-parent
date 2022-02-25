package igloo.hibernateconfig.api;

import java.util.List;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;

public interface IJpaPropertiesProvider {

	Class<? extends Dialect> getDialect();

	String getHbm2Ddl();

	String getHbm2DdlImportFiles();

	Integer getDefaultBatchSize();

	String getHibernateSearchIndexBase();

	Class<? extends Analyzer> getHibernateSearchDefaultAnalyzer();

	String getHibernateSearchIndexingStrategy();
	
	boolean isHibernateSearchIndexInRam();

	String getEhCacheConfiguration();

	boolean isEhCacheSingleton();
	
	Class<? extends RegionFactory> getEhCacheRegionFactory();

	boolean isQueryCacheEnabled();

	String getValidationMode();

	boolean isCreateEmptyCompositesEnabled();

	Class<? extends ImplicitNamingStrategy> getImplicitNamingStrategy();

	Class<? extends PhysicalNamingStrategy> getPhysicalNamingStrategy();

	Class<? extends IdentifierGeneratorStrategyProvider> getIdentifierGeneratorStrategyProvider();

	Boolean isNewGeneratorMappingsEnabled();

	String getDefaultSchema();

	boolean isHibernateSearchElasticSearchEnabled();

	void setHibernateSearchElasticSearchEnabled(boolean isElasticSearchEnabled);

	String getElasticSearchHost();

	void setElasticSearchHost(String elasticSearchHost);

	String getElasticSearchIndexSchemaManagementStrategy();

	void setElasticSearchIndexSchemaManagementStrategy(String elasticSearchIndexSchemaManagementStrategy);

	Properties getDefaultExtraProperties();

	void setDefaultExtraProperties(Properties defaultExtraProperties);

	Properties getExtraProperties();

	void setExtraProperties(Properties extraProperties);

	IntegratorProvider getIntegratorProvider();

	List<TypeContributor> getTypeContributors();

	boolean isXmlMappingEnabled();

}