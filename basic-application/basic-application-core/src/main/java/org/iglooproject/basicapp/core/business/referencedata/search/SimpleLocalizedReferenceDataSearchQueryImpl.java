package org.iglooproject.basicapp.core.business.referencedata.search;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public class SimpleLocalizedReferenceDataSearchQueryImpl
		<
		T extends LocalizedReferenceData<? super T>,
		S extends ISort<SortField>
		>
		extends LocalizedReferenceDataSearchQueryImpl<T, S, ISimpleLocalizedReferenceDataSearchQuery<T, S>> implements ISimpleLocalizedReferenceDataSearchQuery<T, S> {

	public SimpleLocalizedReferenceDataSearchQueryImpl(Class<? extends T> clazz) {
		super(clazz);
	}

}