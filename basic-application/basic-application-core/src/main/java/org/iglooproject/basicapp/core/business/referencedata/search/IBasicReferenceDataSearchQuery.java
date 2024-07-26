package org.iglooproject.basicapp.core.business.referencedata.search;

import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IBasicReferenceDataSearchQuery<
        T extends ReferenceData<? super T>, S extends ISort<?>>
    extends IReferenceDataSearchQuery<T, S, IBasicReferenceDataSearchQuery<T, S>> {}
