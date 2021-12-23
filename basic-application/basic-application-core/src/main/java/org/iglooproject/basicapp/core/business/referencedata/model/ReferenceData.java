package org.iglooproject.basicapp.core.business.referencedata.model;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;

import com.querydsl.core.annotations.QueryInit;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;

@MappedSuperclass
public class ReferenceData<E extends ReferenceData<?>> extends GenericReferenceData<E, LocalizedText> implements IReferenceDataBindingInterface {

	private static final long serialVersionUID = -1779439527249543663L;

	public static final String LABEL = "label";
	public static final String LABEL_PREFIX = LABEL + ".";
	public static final String LABEL_FR_AUTOCOMPLETE = LABEL_PREFIX + LocalizedText.FR_AUTOCOMPLETE;
	public static final String LABEL_FR_SORT = LABEL_PREFIX + LocalizedText.FR_SORT;
	public static final String LABEL_EN_AUTOCOMPLETE = LABEL_PREFIX + LocalizedText.EN_AUTOCOMPLETE;
	public static final String LABEL_EN_SORT = LABEL_PREFIX + LocalizedText.EN_SORT;

	public static final String CODE = "code";
	public static final String CODE_SORT = "codeSort";

	@Embedded
	@IndexedEmbedded(prefix = LABEL_PREFIX)
	@QueryInit("*")
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private LocalizedText label;

	public ReferenceData() {
		this(new LocalizedText());
	}

	public ReferenceData(LocalizedText label) {
		setLabel(label);
	}

	@Override
	public LocalizedText getLabel() {
		if (label == null) {
			label = new LocalizedText();
		}
		return label;
	}

	@Override
	public void setLabel(LocalizedText label) {
		this.label = (label == null ? null : new LocalizedText(label));
	}

	@Override
	@Field(name = CODE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	@Field(name = CODE_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
	@SortableField(forField = CODE_SORT)
	public String getCode() {
		return null;
	}

}
