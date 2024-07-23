package org.iglooproject.spring.property.model;

public final class ImmutablePropertyId<T> extends PropertyId<T>
    implements IImmutablePropertyRegistryKey<T> {

  private static final long serialVersionUID = -582080164746829767L;

  /**
   * This constructor is package-protected. Use {@link AbstractPropertyIds#immutable(String)} for
   * building this type of property ID.
   */
  /*package*/ ImmutablePropertyId(IPropertyRegistryKeyDeclaration declaration, String key) {
    super(declaration, key);
  }

  /**
   * This constructor is package-protected. Use {@link
   * ImmutablePropertyIdTemplate#create(Object...)} for building this type of property ID from a
   * template.
   */
  /*package*/ ImmutablePropertyId(ImmutablePropertyIdTemplate<T> propertyIdTemplate, String key) {
    super(propertyIdTemplate, key);
  }
}
