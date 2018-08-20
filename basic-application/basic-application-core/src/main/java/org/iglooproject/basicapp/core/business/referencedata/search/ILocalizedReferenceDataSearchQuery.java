package org.iglooproject.basicapp.core.business.referencedata.search;

import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.jpa.more.business.referencedata.search.IGenericLocalizedReferenceDataSearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface ILocalizedReferenceDataSearchQuery
		<
		T extends LocalizedReferenceData<? super T>,
		S extends ISort<?>,
		Q extends ILocalizedReferenceDataSearchQuery<T, S, Q>
		>
		extends IGenericLocalizedReferenceDataSearchQuery<T, S, Q> {

	Q label(String label);

	Q code(String code);

}
