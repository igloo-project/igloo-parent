package org.iglooproject.basicapp.core.business.referencedata.model;

import org.hibernate.search.annotations.IndexedEmbedded;
import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;

import com.querydsl.core.annotations.QueryInit;

import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

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
	//TODO: igloo-boot
//	@Field(name = CODE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
//	@Field(name = CODE_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
//	@SortableField(forField = CODE_SORT)
	public String getCode() {
		return null;
	}

}
