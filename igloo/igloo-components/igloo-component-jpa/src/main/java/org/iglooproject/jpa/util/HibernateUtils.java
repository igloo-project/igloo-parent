package org.iglooproject.jpa.util;

import com.google.common.base.Optional;
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
      return (E)
          ((HibernateProxy) potentiallyProxyfiedObject)
              .getHibernateLazyInitializer()
              .getImplementation();
    } else {
      return potentiallyProxyfiedObject;
    }
  }

  @SuppressWarnings("unchecked")
  public static <E> Optional<E> cast(Object potentiallyProxyfiedObject, Class<E> acceptableClass) {
    return cast(potentiallyProxyfiedObject, acceptableClass, new Class[0]);
  }

  @SafeVarargs
  public static <E> Optional<E> cast(
      Object potentiallyProxyfiedObject,
      Class<? extends E> acceptableClass,
      Class<? extends E>... otherAcceptableClasses) {
    Object unwrapped = unwrap(potentiallyProxyfiedObject);
    if (acceptableClass.isInstance(unwrapped)) {
      return Optional.of((E) acceptableClass.cast(unwrapped));
    }
    for (Class<? extends E> otherAcceptableClass : otherAcceptableClasses) {
      if (otherAcceptableClass.isInstance(unwrapped)) {
        return Optional.of((E) otherAcceptableClass.cast(unwrapped));
      }
    }
    return Optional.absent();
  }

  public static void initialize(Object potentiallyProxyfiedObject) {
    Hibernate.initialize(potentiallyProxyfiedObject);

    // Initialize wrapped collections (Collections.unmodifiableCollection, for instance)
    if (potentiallyProxyfiedObject instanceof Collection) {
      ((Collection<?>) potentiallyProxyfiedObject).iterator();
    }
    // Initialize wrapped maps
    if (potentiallyProxyfiedObject instanceof Map) {
      ((Map<?, ?>) potentiallyProxyfiedObject).entrySet().iterator();
    }
  }

  private HibernateUtils() {}
}
