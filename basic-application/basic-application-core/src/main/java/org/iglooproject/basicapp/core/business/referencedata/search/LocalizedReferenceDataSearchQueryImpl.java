package org.iglooproject.basicapp.core.business.referencedata.search;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.jpa.more.business.referencedata.search.GenericLocalizedReferenceDataSearchQueryImpl;
import org.iglooproject.jpa.more.business.sort.ISort;

import com.google.common.collect.Lists;

public class LocalizedReferenceDataSearchQueryImpl
		<
		T extends LocalizedReferenceData<? super T>,
		S extends ISort<SortField>,
		Q extends ILocalizedReferenceDataSearchQuery<T, S, Q>
		>
		extends GenericLocalizedReferenceDataSearchQueryImpl<T, S, Q> implements ILocalizedReferenceDataSearchQuery<T, S, Q> {

	public LocalizedReferenceDataSearchQueryImpl(Class<? extends T> clazz) {
		super(clazz);
	}

	@Override
	public Q label(String label) {
		must(matchAutocompleteIfGiven(label, Lists.newArrayList(LocalizedReferenceData.LABEL_FR_AUTOCOMPLETE, LocalizedReferenceData.LABEL_EN_AUTOCOMPLETE)));
		return thisAsQ();
	}

	@Override
	public Q code(String code) {
		must(matchIfGiven(LocalizedReferenceData.CODE, code));
		return thisAsQ();
	}

}