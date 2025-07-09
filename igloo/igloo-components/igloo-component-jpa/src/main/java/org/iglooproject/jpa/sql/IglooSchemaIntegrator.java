package org.iglooproject.jpa.sql;

import org.apache.commons.lang3.stream.Streams;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.integrator.spi.IntegratorService;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class IglooSchemaIntegrator implements Integrator {

  private final IglooSchemaTool schemaTool = new IglooSchemaTool();

  public static IglooSchemaTool extractSchemaTool(ServiceRegistry serviceRegistry) {
    return Streams.of(serviceRegistry.getService(IntegratorService.class).getIntegrators())
        .filter(IglooSchemaIntegrator.class::isInstance)
        .map(IglooSchemaIntegrator.class::cast)
        .findFirst()
        .orElseThrow()
        .getSchemaTool();
  }

  public static void addSchemaContributor(
      ServiceRegistry serviceRegistry, SchemaContributor schemaContributor) {
    extractSchemaTool(serviceRegistry).addContributor(schemaContributor);
  }

  public IglooSchemaTool getSchemaTool() {
    return schemaTool;
  }

  @Override
  public void integrate(
      Metadata metadata,
      BootstrapContext bootstrapContext,
      SessionFactoryImplementor sessionFactory) {
    // nothing; integragor is just here to provide schemaTool.
  }

  @Override
  public void disintegrate(
      SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
    // nothing
  }
}
