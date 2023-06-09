package org.iglooproject.basicapp.core.business.common.model.embeddable;

import java.util.Collection;
import java.util.Locale;

import org.bindgen.Bindable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.basicapp.core.business.common.util.BasicApplicationLocale;
import org.iglooproject.jpa.more.business.localization.model.AbstractLocalizedText;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

@MappedSuperclass
@Bindable
public class LocalizedText extends AbstractLocalizedText {

	private static final long serialVersionUID = -1225434649910707113L;

	public static final String FR = "fr";
	public static final String FR_AUTOCOMPLETE = "frAutocomplete";
	public static final String FR_SORT = "frSort";

	public static final String EN = "en";
	public static final String EN_AUTOCOMPLETE = "enAutocomplete";
	public static final String EN_SORT = "enSort";

	@Column
	@FullTextField(name = FR, analyzer = HibernateSearchAnalyzer.TEXT_STEMMING)
	@FullTextField(name = FR_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
	@KeywordField(name = FR_SORT, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private String fr;

	@Column
	@FullTextField(name = EN, analyzer = HibernateSearchAnalyzer.TEXT_STEMMING)
	@FullTextField(name = EN_AUTOCOMPLETE, analyzer = HibernateSearchAnalyzer.TEXT)
	@KeywordField(name = EN_SORT, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private String en;

	public LocalizedText() {
		super();
	}

	public LocalizedText(LocalizedText reference) {
		setFr(reference.getFr());
		setEn(reference.getEn());
	}

	@Override
	@Transient
	public Collection<Locale> getSupportedLocales() {
		return BasicApplicationLocale.ALL;
	}

	public String getFr() {
		return fr;
	}

	public void setFr(String fr) {
		this.fr = fr;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	@Override
	@Transient
	public String get(Locale locale) {
		if (locale == null) {
			throw new IllegalArgumentException("Locale should not be null");
		}
		
		if (BasicApplicationLocale.FRENCH.getLanguage().equals(locale.getLanguage())) {
			return getFr();
		} else if (BasicApplicationLocale.ENGLISH.getLanguage().equals(locale.getLanguage())) {
			return getEn();
		} else {
			return get(BasicApplicationLocale.DEFAULT);
		}
	}

	@Override
	@Transient
	public void set(Locale locale, String text) {
		if (locale == null) {
			throw new IllegalArgumentException("Locale should not be null");
		}
		
		if (BasicApplicationLocale.FRENCH.getLanguage().equals(locale.getLanguage())) {
			setFr(text);
		} else if (BasicApplicationLocale.ENGLISH.getLanguage().equals(locale.getLanguage())) {
			setEn(text);
		} else {
			throw new IllegalArgumentException(String.format("Unknown locale: %s", locale));
		}
	}

}
