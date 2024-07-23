package org.iglooproject.basicapp.core.business.referencedata.search;

import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.jpa.more.business.referencedata.search.IGenericReferenceDataSearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;

public interface IReferenceDataSearchQuery<
        T extends ReferenceData<? super T>,
        S extends ISort<?>,
        Q extends IReferenceDataSearchQuery<T, S, Q>>
    extends IGenericReferenceDataSearchQuery<T, S, Q> {

  Q label(String label);

  Q code(String code);
}
