package org.iglooproject.basicapp.core.business.referencedata.search;

import org.apache.lucene.search.SortField;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.jpa.more.business.sort.ISort;

public class BasicReferenceDataSearchQueryImpl<
        T extends ReferenceData<? super T>, S extends ISort<SortField>>
    extends ReferenceDataSearchQueryImpl<T, S, IBasicReferenceDataSearchQuery<T, S>>
    implements IBasicReferenceDataSearchQuery<T, S> {

  public BasicReferenceDataSearchQueryImpl(Class<? extends T> clazz) {
    super(clazz);
  }
}
