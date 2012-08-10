package fr.openwide.core.jpa.hibernate.ejb;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

import org.hibernate.Interceptor;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Permet d'injecter un Interceptor Hibernate géré par Spring avant de créer l'EntityManagerFactory
 * en déclarant cette classe en tant que @Bean
 * http://blog.krecan.net/2009/01/24/spring-managed-hibernate-interceptor-in-jpa/
 *
 */
@SuppressWarnings("deprecation")
public class InterceptorAwareHibernatePersistence extends HibernatePersistence {

	@Autowired
	private Interceptor interceptor;

	@SuppressWarnings("rawtypes")
	@Override
	public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
		Ejb3Configuration cfg = new Ejb3Configuration();
		Ejb3Configuration configured = cfg.configure( info, properties );
		configured.setInterceptor(interceptor);
		return configured != null ? configured.buildEntityManagerFactory() : null;
	}

}
