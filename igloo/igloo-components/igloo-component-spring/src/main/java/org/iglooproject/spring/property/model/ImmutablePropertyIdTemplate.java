package org.iglooproject.spring.property.model;

public final class ImmutablePropertyIdTemplate<T>
    extends PropertyIdTemplate<T, ImmutablePropertyId<T>>
    implements IImmutablePropertyRegistryKey<T> {

  private static final long serialVersionUID = -4239136517475520257L;

  /**
   * This constructor is package-protected. Use {@link
   * AbstractPropertyIds#immutableTemplate(String)} for building this type of property ID template.
   */
  /*package*/ ImmutablePropertyIdTemplate(
      IPropertyRegistryKeyDeclaration declaration, String format) {
    super(declaration, format);
  }

  @Override
  protected ImmutablePropertyId<T> create(String key) {
    return new ImmutablePropertyId<>(this, key);
  }
}
