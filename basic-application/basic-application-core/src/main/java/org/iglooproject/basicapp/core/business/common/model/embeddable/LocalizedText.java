package org.iglooproject.basicapp.core.business.common.model.embeddable;

import java.util.Collection;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.SortableField;

import org.iglooproject.basicapp.core.business.common.util.BasicApplicationLocale;
import org.iglooproject.jpa.more.business.localization.model.AbstractLocalizedText;
import org.iglooproject.jpa.search.util.HibernateSearchAnalyzer;

@MappedSuperclass
@Bindable
public class LocalizedText extends AbstractLocalizedText {

	private static final long serialVersionUID = -1225434649910707113L;

	public static final String FR_AUTOCOMPLETE = "frAutocomplete";
	public static final String EN_AUTOCOMPLETE = "enAutocomplete";

	public static final String FR_SORT = "frSort";
	public static final String EN_SORT = "enSort";

	@Column
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_STEMMING)),
		@Field(name = FR_SORT, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT)),
		@Field(name = FR_AUTOCOMPLETE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	})
	@SortableField(forField = FR_SORT)
	private String fr;

	@Column
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_STEMMING)),
		@Field(name = EN_SORT, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT)),
		@Field(name = EN_AUTOCOMPLETE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	})
	@SortableField(forField = EN_SORT)
	private String en;

	public LocalizedText() {
		super();
	}

	public LocalizedText(LocalizedText reference) {
		setFr(reference.getFr());
		setEn(reference.getEn());
	}

	@Override
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
	public String get(Locale locale) {
		if (locale == null) {
			throw new IllegalArgumentException("Locale should not be null");
		}
		
		if (BasicApplicationLocale.FRENCH.getLanguage().equals(locale.getLanguage())) {
			return getFr();
		} else if (BasicApplicationLocale.ENGLISH.getLanguage().equals(locale.getLanguage())) {
			return getEn();
		} else {
			throw new IllegalArgumentException(String.format("Unknown locale: %s", locale));
		}
	}

	@Override
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