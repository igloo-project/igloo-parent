package org.iglooproject.basicapp.core.business.referencedata.search;

import org.iglooproject.basicapp.core.business.referencedata.model.LocalizedReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface ISimpleLocalizedReferenceDataSearchQuery
		<
		T extends LocalizedReferenceData<? super T>,
		S extends ISort<?>
		>
		extends ILocalizedReferenceDataSearchQuery<T, S, ISimpleLocalizedReferenceDataSearchQuery<T, S>> {

}
