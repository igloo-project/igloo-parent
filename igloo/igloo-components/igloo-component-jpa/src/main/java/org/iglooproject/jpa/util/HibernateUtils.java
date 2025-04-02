package org.iglooproject.jpa.util;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.hibernate.Hibernate;

public final class HibernateUtils {

  public static Class<?> getClass(Object proxy) {
    return Hibernate.getClass(proxy);
  }

  @SuppressWarnings("unchecked")
  public static <E> E unwrap(E proxy) {
    return (E) Hibernate.unproxy(proxy);
  }

  @SuppressWarnings("unchecked")
  public static <E> Optional<E> cast(Object proxy, Class<E> acceptableClass) {
    return cast(proxy, acceptableClass, new Class[0]);
  }

  @SafeVarargs
  public static <E> Optional<E> cast(
      Object proxy,
      Class<? extends E> acceptableClass,
      Class<? extends E>... otherAcceptableClasses) {
    Object unwrapped = unwrap(proxy);
    if (acceptableClass.isInstance(unwrapped)) {
      return Optional.of((E) acceptableClass.cast(unwrapped));
    }
    for (Class<? extends E> otherAcceptableClass : otherAcceptableClasses) {
      if (otherAcceptableClass.isInstance(unwrapped)) {
        return Optional.of((E) otherAcceptableClass.cast(unwrapped));
      }
    }
    return Optional.empty();
  }

  public static void initialize(Object proxy) {
    Hibernate.initialize(proxy);

    // Initialize wrapped collections (Collections.unmodifiableCollection, for instance)
    if (proxy instanceof Collection) {
      ((Collection<?>) proxy).iterator(); // NOSONAR
    }
    // Initialize wrapped maps
    if (proxy instanceof Map) {
      ((Map<?, ?>) proxy).entrySet().iterator(); // NOSONAR
    }
  }

  private HibernateUtils() {}
}
