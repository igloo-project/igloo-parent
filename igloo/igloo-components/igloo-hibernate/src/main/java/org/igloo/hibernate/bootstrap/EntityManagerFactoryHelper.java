package org.igloo.hibernate.bootstrap;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.jpa.boot.spi.Bootstrap;

/**
 * Pattern extracted from hibernate-orm tests for EntityManagerFactory instantiation.
 */
public class EntityManagerFactoryHelper {

	private EntityManagerFactoryHelper() {}

	/**
	 * @param settings provided as key/values pairs: Object[] must be even-sized, with alternating key/value objects.
	 * 
	 * @return A fresh EntityManagerFactory
	 */
	public static EntityManagerFactory generateEntityManagerFactory(Object... settings) {
		return Bootstrap.getEntityManagerFactoryBuilder(new PersistenceUnitDescriptorAdapter(), generateSettings(settings)).build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map generateSettings(Object... keysAndValues) {
		final Map settings = new HashMap();

		if ( keysAndValues != null ) {
			if ( keysAndValues.length %2 != 0 ) {
				throw new IllegalStateException("Varargs to create settings should contain even number of entries");
			}


			for ( int i = 0; i < keysAndValues.length; ) {
				settings.put( keysAndValues[i], keysAndValues[i+1] );
				i+=2;
			}
		}

		return settings;
	}
}
