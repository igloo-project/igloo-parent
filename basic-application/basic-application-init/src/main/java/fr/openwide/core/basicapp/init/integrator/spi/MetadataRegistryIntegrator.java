package fr.openwide.core.basicapp.init.integrator.spi;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/*
 * Override Integrator used for handle the metadata object required by the BasicApplicationSqlUpdateScriptMain.java
 * The path to this class is define in the file META-INF/services/org.hibernate.integrator.spi.Integrator
 */
public class MetadataRegistryIntegrator implements org.hibernate.integrator.spi.Integrator {

	public static Metadata METADATA;

	@Override
	public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry) {
		METADATA = metadata;
	}

	@Override
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
	}

}
