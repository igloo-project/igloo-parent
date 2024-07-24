package org.iglooproject.jpa.more.business.referencedata.search;

import java.util.Map;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;

public interface IGenericReferenceDataSearchQuery<
        T extends GenericReferenceData<? super T, ?>,
        S extends ISort<?>,
        Q extends IGenericReferenceDataSearchQuery<T, S, Q>>
    extends ISearchQuery<T, S> {

  Q enabled(EnabledFilter enabledFilter);

  @Override
  Q sort(Map<S, SortOrder> sortMap);
}
