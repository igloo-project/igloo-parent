package org.igloo.hibernate.hbm;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
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
      BootstrapContext bootstrapContext,
      SessionFactoryImplementor sessionFactory) {
    this.metadata = metadata;
  }

  @Override
  public void disintegrate(
      SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
    // nothing
  }

  public Metadata getMetadata() {
    return metadata;
  }
}
