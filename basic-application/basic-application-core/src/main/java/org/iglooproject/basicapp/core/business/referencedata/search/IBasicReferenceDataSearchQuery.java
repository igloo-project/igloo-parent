package org.iglooproject.basicapp.core.business.referencedata.search;

import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;

public interface IBasicReferenceDataSearchQuery<T extends ReferenceData<? super T>> extends IAbstractReferenceDataSearchQuery<T, BasicReferenceDataSort, BasicReferenceDataSearchQueryData<T>> {

}
