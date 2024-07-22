package basicapp.back.business.referencedata.search;

import basicapp.back.business.referencedata.model.ReferenceData;

public class BasicReferenceDataSearchQueryData<T extends ReferenceData<? super T>>
    extends AbstractReferenceDataSearchQueryData<T>
    implements IBasicReferenceDataSearchQueryDataBindingInterface {}
