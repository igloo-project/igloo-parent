package org.iglooproject.jpa.config.spring.convert.converter;

import java.io.Serializable;
import org.apache.commons.lang3.tuple.Pair;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.springframework.core.GenericTypeResolver;

public class GenericEntityTypeResolver {

  private GenericEntityTypeResolver() {}

  /**
   * @return the resolved entity key type parameter (K) and entity type parameter (E), or {@code
   *     null} if not resolvable
   */
  @SuppressWarnings("unchecked")
  public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>>
      Pair<Class<K>, Class<E>> resolveTypeParameters(Class<? extends E> clazz) {
    Class<?>[] typeParameters =
        GenericTypeResolver.resolveTypeArguments(clazz, GenericEntity.class);
    return typeParameters == null
        ? null
        : Pair.of((Class<K>) typeParameters[0], (Class<E>) typeParameters[1]);
  }

  /**
   * @return the resolved entity key type parameter (K), or {@code null} if not resolvable
   */
  @SuppressWarnings("unchecked")
  public static <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
      Class<K> resolveKeyTypeParameter(Class<E> clazz) {
    Class<?>[] typeParameters =
        GenericTypeResolver.resolveTypeArguments(clazz, GenericEntity.class);
    return typeParameters == null ? null : (Class<K>) typeParameters[0];
  }
}
