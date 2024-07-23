package org.iglooproject.jpa.config.spring.provider;

import java.sql.Driver;

public interface IDatabaseConnectionJndiConfigurationProvider
    extends IDatabaseConnectionConfigurationProvider {

  String getJndiName();

  @Override
  Class<Driver> getDriverClass();
}
