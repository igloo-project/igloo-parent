package org.iglooproject.spring.property.model;

public final class MutablePropertyId<T> extends PropertyId<T>
    implements IMutablePropertyRegistryKey<T> {

  private static final long serialVersionUID = 7635126176447950686L;

  /**
   * This constructor is package-protected. Use {@link AbstractPropertyIds#mutable(String)} for
   * building this type of property ID.
   */
  /*package*/ MutablePropertyId(IPropertyRegistryKeyDeclaration declaration, String key) {
    super(declaration, key);
  }

  /**
   * This constructor is package-protected. Use {@link MutablePropertyIdTemplate#create(Object...)}
   * for building this type of property ID from a template.
   */
  /*package*/ MutablePropertyId(MutablePropertyIdTemplate<T> propertyIdTemplate, String key) {
    super(propertyIdTemplate, key);
  }
}
