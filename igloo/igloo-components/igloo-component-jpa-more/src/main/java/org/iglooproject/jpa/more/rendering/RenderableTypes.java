package org.iglooproject.jpa.more.rendering;

import java.util.Arrays;
import java.util.stream.Stream;

/** Helpers to walk the type hierarchy of a rendered value when looking for a renderer. */
public final class RenderableTypes {

  private RenderableTypes() {}

  /**
   * Candidate types for a renderer lookup, in lookup order: interfaces extending {@link
   * IRenderable} (each one followed by its own super-interfaces), then the superclass followed by
   * its own hierarchy. The given type itself is not included.
   */
  public static Stream<Class<?>> hierarchy(Class<?> type) {
    Stream<Class<?>> interfaces =
        Arrays.stream(type.getInterfaces())
            .filter(IRenderable.class::isAssignableFrom)
            .flatMap(iface -> Stream.concat(Stream.of(iface), hierarchy(iface)));
    Class<?> superclass = type.getSuperclass();
    Stream<Class<?>> superclasses =
        superclass == null || superclass == Object.class
            ? Stream.empty()
            : Stream.concat(Stream.of(superclass), hierarchy(superclass));
    return Stream.concat(interfaces, superclasses);
  }
}
