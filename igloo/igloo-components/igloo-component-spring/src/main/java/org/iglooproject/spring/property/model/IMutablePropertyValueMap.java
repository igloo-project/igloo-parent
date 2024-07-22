package org.iglooproject.spring.property.model;

import java.util.Map;

/** Type-safe map of property ids to values. */
public interface IMutablePropertyValueMap extends Iterable<IMutablePropertyValueMap.Entry<?>> {

  /**
   * @see Map#get(Object)
   */
  <T> T get(MutablePropertyId<T> key);

  /**
   * @see Map#put(Object, Object)
   */
  <T> T put(MutablePropertyId<T> key, T value);

  interface Entry<T> {
    /**
     * @see Map.Entry#getKey()
     */
    MutablePropertyId<T> getKey();

    /**
     * @see Map.Entry#getValue()
     */
    T getValue();
  }
}
