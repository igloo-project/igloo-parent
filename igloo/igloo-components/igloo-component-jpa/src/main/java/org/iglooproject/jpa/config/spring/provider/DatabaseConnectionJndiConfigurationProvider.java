package org.iglooproject.jpa.config.spring.provider;

import org.springframework.beans.factory.annotation.Value;

public class DatabaseConnectionJndiConfigurationProvider
    extends AbstractDatabaseConnectionConfigurationProvider
    implements IDatabaseConnectionJndiConfigurationProvider {

  @Value("${db.jndiName}")
  private String jndiName;

  @Override
  public String getJndiName() {
    return jndiName;
  }
}
