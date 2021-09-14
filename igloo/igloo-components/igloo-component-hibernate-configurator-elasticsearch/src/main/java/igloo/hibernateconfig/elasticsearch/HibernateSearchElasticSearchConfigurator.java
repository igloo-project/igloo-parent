package igloo.hibernateconfig.elasticsearch;

import java.util.Properties;

import org.hibernate.search.elasticsearch.cfg.ElasticsearchEnvironment;

import igloo.hibernateconfig.api.HibernateSearchBackend;
import igloo.hibernateconfig.api.HibernateSearchConfigurator;
import igloo.hibernateconfig.api.IJpaPropertiesProvider;

public class HibernateSearchElasticSearchConfigurator implements HibernateSearchConfigurator {

	@Override
	public HibernateSearchBackend getBackend() {
		return HibernateSearchBackend.ELASTICSEARCH;
	}

	@Override
	public void configureHibernateSearch(IJpaPropertiesProvider configuration, Properties properties) {
		properties.setProperty(ElasticsearchEnvironment.ANALYSIS_DEFINITION_PROVIDER, CoreElasticSearchAnalyzersDefinitionProvider.class.getName());
		properties.setProperty("hibernate.search.default.indexmanager", "elasticsearch");
		properties.setProperty("hibernate.search.default.elasticsearch.host", configuration.getElasticSearchHost());
		properties.setProperty("hibernate.search.default.elasticsearch.index_schema_management_strategy", configuration.getElasticSearchIndexSchemaManagementStrategy());
	}

}
