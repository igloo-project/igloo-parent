package org.iglooproject.spring.property.model;

import java.io.Serializable;
import java.util.Set;
import org.iglooproject.spring.property.service.IPropertyRegistry;

/**
 * Represents a place were {@link IPropertyRegistryKey property registry keys} are declared.
 *
 * <p>Helps detecting missing registrations in the {@link IPropertyRegistry}.
 */
public interface IPropertyRegistryKeyDeclaration extends Serializable {

  Set<IPropertyRegistryKey<?>> getDeclaredKeys();

  /**
   * @return True if <code>obj</code> also is a declaration and refers to the same declaration as
   *     <code>this</code>.
   */
  @Override
  boolean equals(Object obj);

  @Override
  public String toString();
}
