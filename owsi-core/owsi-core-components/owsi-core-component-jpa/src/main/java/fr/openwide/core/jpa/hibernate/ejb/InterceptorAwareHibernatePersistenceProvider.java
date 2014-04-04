package fr.openwide.core.jpa.hibernate.ejb;

import java.util.Map;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.cfg.Configuration;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.hibernate.service.ServiceRegistry;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Inject an Hibernate Interceptor managed by Spring in the SessionFactory.
 * 
 * The original idea came from the following blog:
 * http://blog.krecan.net/2009/01
 * /24/spring-managed-hibernate-interceptor-in-jpa/
 * but has been completely overhauled with the ORM 4.3 upgrade.
 */
public class InterceptorAwareHibernatePersistenceProvider extends HibernatePersistenceProvider {

	private static final Logger LOGGER = Logger.getLogger(InterceptorAwareHibernatePersistenceProvider.class);

	@Autowired
	private Interceptor interceptor;

	@Override
	@SuppressWarnings("rawtypes")
	public EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(
			PersistenceUnitDescriptor persistenceUnitDescriptor, Map integration, ClassLoader providedClassLoader) {
		return new EntityManagerFactoryBuilderImpl(persistenceUnitDescriptor, integration, providedClassLoader) {
			@Override
			public Configuration buildHibernateConfiguration(ServiceRegistry serviceRegistry) {
				Configuration configuration = super.buildHibernateConfiguration(serviceRegistry);
				if (InterceptorAwareHibernatePersistenceProvider.this.interceptor != null) {
					if (configuration.getInterceptor() != null
							&& !EmptyInterceptor.class.equals(configuration.getInterceptor().getClass())) {
						LOGGER.error("The persistence provider was already configured with an interceptor: we override it.");
					}
					configuration.setInterceptor(InterceptorAwareHibernatePersistenceProvider.this.interceptor);
				}
				return configuration;
			}
		};
	}

}
