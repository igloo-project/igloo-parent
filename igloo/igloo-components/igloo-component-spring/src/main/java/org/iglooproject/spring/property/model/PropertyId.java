package org.iglooproject.spring.property.model;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class PropertyId<T> implements IPropertyRegistryKey<T> {

  private static final long serialVersionUID = 995641080772195895L;

  private final IPropertyRegistryKeyDeclaration declaration;

  private final String key;

  private final PropertyIdTemplate<T, ?> template;

  /**
   * This constructor is package-protected. Use {@link AbstractPropertyIds#immutable(String)} or
   * {@link AbstractPropertyIds#mutable(String)} for building a property ID.
   */
  /*package*/ PropertyId(IPropertyRegistryKeyDeclaration declaration, String key) {
    Preconditions.checkNotNull(declaration);
    Preconditions.checkNotNull(key);
    this.declaration = declaration;
    this.key = key;
    this.template = null;
  }

  /**
   * This constructor is package-protected. Use {@link
   * ImmutablePropertyIdTemplate#create(Object...)} or {@link
   * MutablePropertyIdTemplate#create(Object...)} for building a property ID from a template.
   */
  /*package*/ PropertyId(PropertyIdTemplate<T, ?> template, String key) {
    Preconditions.checkNotNull(template);
    Preconditions.checkNotNull(key);
    this.declaration = null;
    this.key = key;
    this.template = template;
  }

  @Override
  public IPropertyRegistryKeyDeclaration getDeclaration() {
    return declaration;
  }

  public String getKey() {
    return key;
  }

  public PropertyIdTemplate<T, ?> getTemplate() {
    return template;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PropertyId<?> other)) {
      return false;
    }
    return Objects.equals(getClass(), other.getClass())
        && Objects.equals(key, other.key)
        && Objects.equals(template, other.template);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), key, template);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("declared by", declaration)
        .append("template", template)
        .append("key", key)
        .build();
  }
}
