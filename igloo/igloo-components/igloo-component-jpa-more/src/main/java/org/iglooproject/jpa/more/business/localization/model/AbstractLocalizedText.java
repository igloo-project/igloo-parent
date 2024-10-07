package org.iglooproject.jpa.more.business.localization.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import jakarta.persistence.Transient;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bindgen.Bindable;
import org.springframework.util.StringUtils;

@Bindable
public abstract class AbstractLocalizedText implements ILocalizedText {

  private static final long serialVersionUID = -4433811130100791706L;

  @Transient
  public abstract Collection<Locale> getSupportedLocales();

  @Override
  @Transient
  public abstract String get(Locale locale);

  @Transient
  public static String get(AbstractLocalizedText text, Locale locale) {
    if (text == null) {
      return null;
    } else {
      return text.get(locale);
    }
  }

  @Transient
  public abstract void set(Locale locale, String text);

  @Transient
  public String getOrDefault(Locale locale) {
    List<Locale> locales = Lists.newArrayList(locale);
    locales.addAll(getSupportedLocales());
    return getFirstNonEmpty(locales);
  }

  @Transient
  public String getFirstNonEmpty(Collection<Locale> locales) {
    if (locales == null) {
      throw new IllegalArgumentException("Locales collection should not be null");
    }

    for (Locale locale : locales) {
      String text = get(locale);
      if (StringUtils.hasText(text)) {
        return text;
      }
    }

    return null;
  }

  /** Null-safe version of {@link #getFirstNonEmpty(Collection)}. */
  @Transient
  public static String getFirstNonEmpty(AbstractLocalizedText text, Collection<Locale> locales) {
    if (text == null) {
      return null;
    } else {
      return text.getFirstNonEmpty(locales);
    }
  }

  @Transient
  public boolean hasContent() {
    for (Locale locale : getSupportedLocales()) {
      if (hasContent(locale)) {
        return true;
      }
    }

    return false;
  }

  @Transient
  public boolean hasContent(Locale locale) {
    return hasContent(get(locale));
  }

  @Transient
  protected boolean hasContent(String value) {
    return StringUtils.hasText(value);
  }

  @Override
  @Transient
  public String toString() {
    ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    for (Locale locale : getSupportedLocales()) {
      builder = builder.append(locale.getLanguage(), get(locale));
    }
    return builder.build();
  }

  @Override
  @Transient
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!getClass().isInstance(obj)) {
      return false;
    }
    AbstractLocalizedText other = (AbstractLocalizedText) obj;

    for (Locale locale : getSupportedLocales()) {
      if (!Objects.equal(get(locale), other.get(locale))) {
        return false;
      }
    }

    return true;
  }

  @Override
  @Transient
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    for (Locale locale : getSupportedLocales()) {
      builder = builder.append(get(locale));
    }
    return builder.build();
  }

  @Override
  @Transient
  public AbstractLocalizedText clone() { // NOSONAR
    try {
      return (AbstractLocalizedText) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }
}
