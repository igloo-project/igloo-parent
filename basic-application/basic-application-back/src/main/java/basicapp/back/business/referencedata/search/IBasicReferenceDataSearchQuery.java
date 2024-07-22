package basicapp.back.business.referencedata.search;

import basicapp.back.business.referencedata.model.ReferenceData;

public interface IBasicReferenceDataSearchQuery<T extends ReferenceData<? super T>>
    extends IAbstractReferenceDataSearchQuery<
        T, BasicReferenceDataSort, BasicReferenceDataSearchQueryData<T>> {}
