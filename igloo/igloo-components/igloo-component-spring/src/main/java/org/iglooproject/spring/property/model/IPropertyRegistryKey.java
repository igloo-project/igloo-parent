package org.iglooproject.spring.property.model;

import java.io.Serializable;

/** Object wrapping a key matching a property from a resource file or from the database. */
public interface IPropertyRegistryKey<T> extends Serializable {

  /**
   * @return The declaration for this key, or null in the case of {@link PropertyIdTemplate
   *     template}-generated keys.
   */
  public IPropertyRegistryKeyDeclaration getDeclaration();
}
