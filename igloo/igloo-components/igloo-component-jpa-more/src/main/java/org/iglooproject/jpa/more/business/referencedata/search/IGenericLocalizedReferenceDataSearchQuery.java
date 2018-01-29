package org.iglooproject.jpa.more.business.referencedata.search;

import org.iglooproject.jpa.more.business.referencedata.model.GenericLocalizedReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IGenericLocalizedReferenceDataSearchQuery
		<
		T extends GenericLocalizedReferenceData<? super T, ?>,
		S extends ISort<?>,
		Q extends IGenericLocalizedReferenceDataSearchQuery<T, S, Q>
		>
		extends IGenericReferenceDataSearchQuery<T, S, Q> {

}
