package igloo.hibernateconfig.lucene;

import java.util.Properties;

import org.hibernate.search.store.impl.FSDirectoryProvider;
import org.hibernate.search.store.impl.RAMDirectoryProvider;

import igloo.hibernateconfig.api.HibernateSearchBackend;
import igloo.hibernateconfig.api.HibernateSearchConfigurator;
import igloo.hibernateconfig.api.IJpaPropertiesProvider;

public class HibernateSearchLuceneConfigurator implements HibernateSearchConfigurator {

	@Override
	public HibernateSearchBackend getBackend() {
		return HibernateSearchBackend.LUCENE;
	}

	@Override
	public void configureHibernateSearch(IJpaPropertiesProvider configuration, Properties properties) {
		properties.setProperty(org.hibernate.search.cfg.Environment.ANALYSIS_DEFINITION_PROVIDER, CoreLuceneAnalyzersDefinitionProvider.class.getName());
		if (configuration.isHibernateSearchIndexInRam()) {
			properties.setProperty("hibernate.search.default.directory_provider", RAMDirectoryProvider.class.getName());
		} else {
			properties.setProperty("hibernate.search.default.directory_provider", FSDirectoryProvider.class.getName());
			properties.setProperty("hibernate.search.default.locking_strategy", "native");
		}
		
		properties.setProperty("hibernate.search.default.indexBase", configuration.getHibernateSearchIndexBase());
		properties.setProperty("hibernate.search.default.exclusive_index_use", Boolean.TRUE.toString());
		properties.setProperty(org.hibernate.search.cfg.Environment.LUCENE_MATCH_VERSION,
				org.hibernate.search.cfg.Environment.DEFAULT_LUCENE_MATCH_VERSION.toString());
	}

}
