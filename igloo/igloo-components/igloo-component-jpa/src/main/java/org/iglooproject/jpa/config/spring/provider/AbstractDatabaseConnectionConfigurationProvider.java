package org.iglooproject.jpa.config.spring.provider;

import java.sql.Driver;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractDatabaseConnectionConfigurationProvider
    implements IDatabaseConnectionConfigurationProvider {

  @Value("${db.datasourceProvider}")
  private DatasourceProvider provider = DatasourceProvider.APPLICATION;

  @Value("${${db.type}.db.driverClass}")
  private Class<Driver> driverClass;

  public AbstractDatabaseConnectionConfigurationProvider() {
    super();
  }

  @Override
  public Class<Driver> getDriverClass() {
    return driverClass;
  }

  @Override
  public DatasourceProvider getProvider() {
    return provider;
  }
}
