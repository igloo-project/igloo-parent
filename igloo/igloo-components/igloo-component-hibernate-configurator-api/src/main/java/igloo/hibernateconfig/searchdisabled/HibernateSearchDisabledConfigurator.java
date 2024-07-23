package igloo.hibernateconfig.searchdisabled;

import igloo.hibernateconfig.api.HibernateSearchBackend;
import igloo.hibernateconfig.api.HibernateSearchConfigurator;
import igloo.hibernateconfig.api.IJpaPropertiesProvider;
import java.util.Properties;

public class HibernateSearchDisabledConfigurator implements HibernateSearchConfigurator {

  @Override
  public HibernateSearchBackend getBackend() {
    return HibernateSearchBackend.DISABLED;
  }

  @Override
  public void configureHibernateSearch(
      IJpaPropertiesProvider configuration, Properties properties) {
    properties.setProperty("hibernate.search.autoregister_listeners", Boolean.FALSE.toString());
  }
}
