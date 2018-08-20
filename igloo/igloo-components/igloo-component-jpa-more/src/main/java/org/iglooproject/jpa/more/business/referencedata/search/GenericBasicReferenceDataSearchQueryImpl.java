package org.iglooproject.jpa.more.business.referencedata.search;

import org.apache.lucene.search.SortField;
import org.iglooproject.jpa.more.business.referencedata.model.GenericBasicReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public class GenericBasicReferenceDataSearchQueryImpl
		<
		T extends GenericBasicReferenceData<? super T>,
		S extends ISort<SortField>,
		Q extends IGenericBasicReferenceDataSearchQuery<T, S, Q>
		>
		extends GenericReferenceDataSearchQueryImpl<T, S, Q> implements IGenericBasicReferenceDataSearchQuery<T, S, Q> {

	@SafeVarargs
	public GenericBasicReferenceDataSearchQueryImpl(Class<? extends T> clazz, S... defaultSorts) {
		super(clazz, defaultSorts);
	}

	@SafeVarargs
	public GenericBasicReferenceDataSearchQueryImpl(Class<? extends T>[] classes, S... defaultSorts) {
		super(classes, defaultSorts);
	}

}