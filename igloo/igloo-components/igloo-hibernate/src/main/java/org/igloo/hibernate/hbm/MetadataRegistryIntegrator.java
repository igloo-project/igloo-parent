package org.igloo.hibernate.hbm;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/*
 * Integrator used to register the metadata object required by the BasicApplicationSqlUpdateScriptMain.java
 */
public class MetadataRegistryIntegrator implements org.hibernate.integrator.spi.Integrator {

  private Metadata metadata;

  @Override
  public void integrate(
      Metadata metadata,
      SessionFactoryImplementor sessionFactory,
      SessionFactoryServiceRegistry serviceRegistry) {
    this.metadata = metadata;
  }

  @Override
  public void disintegrate(
      SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {}

  public Metadata getMetadata() {
    return metadata;
  }
}
