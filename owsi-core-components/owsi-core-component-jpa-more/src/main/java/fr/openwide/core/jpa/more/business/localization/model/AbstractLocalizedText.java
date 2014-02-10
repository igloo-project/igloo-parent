package fr.openwide.core.jpa.more.business.localization.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bindgen.Bindable;
import org.springframework.util.StringUtils;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@MappedSuperclass
@Bindable
public abstract class AbstractLocalizedText implements Serializable, Cloneable {

	private static final long serialVersionUID = -4433811130100791706L;

	@Transient
	public abstract Collection<Locale> getSupportedLocales();

	public abstract String get(Locale locale);
	
	/**
	 * Null-safe version of {@link #get(Locale)}.
	 */
	public static String get(AbstractLocalizedText text, Locale locale) {
		if (text == null) {
			return null;
		} else {
			return text.get(locale);
		}
	}

	public abstract void set(Locale locale, String text);
	
	public String getOrDefault(Locale locale) {
		List<Locale> locales = Lists.newArrayList(locale);
		locales.addAll(getSupportedLocales());
		return getFirstNonEmpty(locales);
	}
	
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
	
	/**
	 * Null-safe version of {@link #getFirstNonEmpty(Collection)}.
	 */
	public static String getText(AbstractLocalizedText text, Collection<Locale> locales) {
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

	@Transient
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		for (Locale locale : getSupportedLocales()) {
			builder = builder.append(locale.getLanguage(), get(locale));
		}
		return builder.build();
	}

	@Override
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

	@Transient
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		for (Locale locale : getSupportedLocales()) {
			builder = builder.append(get(locale));
		}
		return builder.build();
	}

	@Transient
	@Override
	public AbstractLocalizedText clone() { // NOSONAR
		try {
			return (AbstractLocalizedText) super.clone();
		} catch(CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

}