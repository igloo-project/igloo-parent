package org.iglooproject.jpa.more.business.referencedata.search;

import org.iglooproject.jpa.more.business.referencedata.model.GenericBasicReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IGenericBasicReferenceDataSearchQuery
		<
		T extends GenericBasicReferenceData<? super T>,
		S extends ISort<?>,
		Q extends IGenericBasicReferenceDataSearchQuery<T, S, Q>
		>
		extends IGenericReferenceDataSearchQuery<T, S, Q> {

}
