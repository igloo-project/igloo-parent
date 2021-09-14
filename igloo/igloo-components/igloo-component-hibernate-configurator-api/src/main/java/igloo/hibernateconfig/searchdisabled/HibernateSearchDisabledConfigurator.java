package igloo.hibernateconfig.searchdisabled;

import java.util.Properties;

import igloo.hibernateconfig.api.HibernateSearchBackend;
import igloo.hibernateconfig.api.HibernateSearchConfigurator;
import igloo.hibernateconfig.api.IJpaPropertiesProvider;

public class HibernateSearchDisabledConfigurator implements HibernateSearchConfigurator {

	@Override
	public HibernateSearchBackend getBackend() {
		return HibernateSearchBackend.DISABLED;
	}

	@Override
	public void configureHibernateSearch(IJpaPropertiesProvider configuration, Properties properties) {
		properties.setProperty("hibernate.search.autoregister_listeners", Boolean.FALSE.toString());
	}

}
