package basicapp.back.business.referencedata.search;

import basicapp.back.business.referencedata.model.ReferenceData;

public class BasicReferenceDataSearchQueryImpl<T extends ReferenceData<? super T>> extends AbstractReferenceDataSearchQueryImpl<T, BasicReferenceDataSort, BasicReferenceDataSearchQueryData<T>> implements IBasicReferenceDataSearchQuery<T> {

	public BasicReferenceDataSearchQueryImpl(Class<T> clazz) {
		super(clazz);
	}

}
