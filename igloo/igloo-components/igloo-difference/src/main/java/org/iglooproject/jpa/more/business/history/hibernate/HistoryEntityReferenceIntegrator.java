package org.iglooproject.jpa.more.business.history.hibernate;

import jakarta.persistence.metamodel.EntityType;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.iglooproject.jpa.sql.IglooSchemaIntegrator;

/**
 * This integrator adds a post-initialization observer to push entities' list to {@link
 * HistoryEntityReferenceTypeJdbcType}. This is done only if optimization is enabled.
 *
 * @see HistoryEntityReferenceTypeJdbcType
 * @see HistoryEntityReferenceTypeContributor
 */
public class HistoryEntityReferenceIntegrator implements Integrator {

  @Override
  public void integrate(
      Metadata metadata,
      BootstrapContext bootstrapContext,
      SessionFactoryImplementor sessionFactory) {
    if (HistoryEntityReferenceTypeContributor.isOptimizationEnabled(
        sessionFactory.getServiceRegistry())) {
      IglooSchemaIntegrator.addSchemaContributor(
          bootstrapContext.getServiceRegistry(), new HistoryEntityReferenceSchemaContributor());
      sessionFactory.addObserver(
          new SessionFactoryObserver() {
            private static final long serialVersionUID = 1L;

            @Override
            public void sessionFactoryCreated(SessionFactory factory) {
              HistoryEntityReferenceTypeJdbcType type =
                  (HistoryEntityReferenceTypeJdbcType)
                      sessionFactory
                          .getTypeConfiguration()
                          .getJdbcTypeRegistry()
                          .getDescriptor(HistoryEntityReferenceTypeJdbcType.SQL_TYPE_CODE);
              @SuppressWarnings("rawtypes")
              List<Class> entities =
                  factory.getMetamodel().getEntities().stream()
                      .<Class>map(EntityType::getJavaType)
                      .toList();
              type.setupTypes(entities);
            }
          });
    }
  }

  @Override
  public void disintegrate(
      SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
    // nothing
  }
}
