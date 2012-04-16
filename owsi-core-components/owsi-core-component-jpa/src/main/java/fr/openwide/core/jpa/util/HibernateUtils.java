package fr.openwide.core.jpa.util;

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
	
	private HibernateUtils() {
	}
}