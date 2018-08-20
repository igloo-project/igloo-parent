package org.iglooproject.jpa.more.business.referencedata.search;

import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.more.business.referencedata.model.GenericLocalizedReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public abstract class GenericLocalizedReferenceDataSearchQueryImpl
		<
		T extends GenericLocalizedReferenceData<? super T, ?>,
		S extends ISort<SortField>,
		Q extends IGenericLocalizedReferenceDataSearchQuery<T, S, Q>
		>
		extends GenericReferenceDataSearchQueryImpl<T, S, Q> implements IGenericLocalizedReferenceDataSearchQuery<T, S, Q> {

	@SafeVarargs
	public GenericLocalizedReferenceDataSearchQueryImpl(Class<? extends T> clazz, S... defaultSorts) {
		super(clazz, defaultSorts);
	}

	@SafeVarargs
	public GenericLocalizedReferenceDataSearchQueryImpl(Class<? extends T>[] classes, S... defaultSorts) {
		super(classes, defaultSorts);
	}

}