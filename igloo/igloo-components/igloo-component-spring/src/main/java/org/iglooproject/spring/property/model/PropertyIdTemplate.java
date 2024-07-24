package org.iglooproject.spring.property.model;

import com.google.common.base.Preconditions;
import java.util.Locale;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** Object to build a {@link PropertyId} on runtime to deal with computed key. */
public abstract class PropertyIdTemplate<T, P extends PropertyId<T>>
    implements IPropertyRegistryKey<T> {

  private static final long serialVersionUID = 6470015401685025239L;

  private final IPropertyRegistryKeyDeclaration declaration;

  private final String format;

  /**
   * This constructor is package-protected. Use {@link
   * AbstractPropertyIds#immutableTemplate(String)} or {@link
   * AbstractPropertyIds#mutableTemplate(String)} for building a property ID template.
   */
  public PropertyIdTemplate(IPropertyRegistryKeyDeclaration declaration, String format) {
    Preconditions.checkNotNull(declaration);
    Preconditions.checkNotNull(format);
    this.declaration = declaration;
    this.format = format;
  }

  @Override
  public IPropertyRegistryKeyDeclaration getDeclaration() {
    return declaration;
  }

  public String getFormat() {
    return format;
  }

  public P create(Object... args) {
    return create(String.format(Locale.ROOT, format, args));
  }

  protected abstract P create(String key);

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof PropertyIdTemplate)) {
      return false;
    }
    PropertyIdTemplate<?, ?> other = (PropertyIdTemplate<?, ?>) obj;
    return new EqualsBuilder().append(format, other.format).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(format).build();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("declared by", declaration)
        .append("format", format)
        .build();
  }
}
