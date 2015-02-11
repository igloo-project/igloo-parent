package fr.openwide.core.jpa.util;

import java.util.Collection;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public final class HibernateUtils {
	
	public static Class<?> getClass(Object potentiallyProxyfiedObject) {
		return Hibernate.getClass(potentiallyProxyfiedObject);
	}
	
	@SuppressWarnings("unchecked")
	public static <E> E unwrap(E potentiallyProxyfiedObject) {
		if (potentiallyProxyfiedObject instanceof HibernateProxy) {
			return (E) ((HibernateProxy) potentiallyProxyfiedObject).getHibernateLazyInitializer().getImplementation();
		} else {
			return potentiallyProxyfiedObject;
		}
	}
	
	public static void initialize(Object potentiallyProxyfiedObject) {
		Hibernate.initialize(potentiallyProxyfiedObject);
		
		// Initialize wrapped collections (Collections.unmodifiableCollection, for instance)
		if (potentiallyProxyfiedObject instanceof Collection) {
			((Collection<?>)potentiallyProxyfiedObject).iterator();
		}
		// Initialize wrapped maps
		if (potentiallyProxyfiedObject instanceof Map) {
			((Map<?,?>)potentiallyProxyfiedObject).entrySet().iterator();
		}
	}
	
	private HibernateUtils() {
	}
}