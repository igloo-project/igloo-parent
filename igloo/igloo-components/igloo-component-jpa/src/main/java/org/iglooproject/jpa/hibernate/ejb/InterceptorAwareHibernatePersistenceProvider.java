package org.iglooproject.jpa.hibernate.ejb;

import java.util.Map;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.spi.PersistenceUnitInfo;

import org.hibernate.Interceptor;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
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

	/**
	 * 2017-05-24 · reworked from SpringHibernateJpaPersistenceProvider so that we can inject a custom
	 * {@link EntityManagerFactoryBuilderImpl}; previous implementation that overrides
	 * {@link InterceptorAwareHibernatePersistenceProvider#getEntityManagerFactoryBuilder} no longer works
	 * as there are several paths with various arguments and the overloaded one was no longer called.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
		return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(info), properties) {
			@Override
			protected void populateSfBuilder(SessionFactoryBuilder sfBuilder, StandardServiceRegistry ssr) {
				super.populateSfBuilder(sfBuilder, ssr);
				
				if (InterceptorAwareHibernatePersistenceProvider.this.interceptor != null) {
					LOGGER.warn("Installing our Spring managed interceptor.");
					sfBuilder.applyInterceptor(InterceptorAwareHibernatePersistenceProvider.this.interceptor);
				}
			}
		}.build();
	}

}
