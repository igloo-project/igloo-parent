package org.iglooproject.jpa.more.business.referencedata.search;

import java.util.Map;
import org.apache.lucene.search.SortField;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;

public abstract class GenericReferenceDataSearchQueryImpl<
        T extends GenericReferenceData<? super T, ?>,
        S extends ISort<SortField>,
        Q extends IGenericReferenceDataSearchQuery<T, S, Q>>
    extends AbstractHibernateSearchSearchQuery<T, S>
    implements IGenericReferenceDataSearchQuery<T, S, Q> {

  @SafeVarargs
  public GenericReferenceDataSearchQueryImpl(Class<? extends T> clazz, S... defaultSorts) {
    super(clazz, defaultSorts);
  }

  @SafeVarargs
  public GenericReferenceDataSearchQueryImpl(Class<? extends T>[] classes, S... defaultSorts) {
    super(classes, defaultSorts);
  }

  @SuppressWarnings("unchecked")
  protected final Q thisAsQ() {
    return (Q) this;
  }

  @Override
  public Q enabled(EnabledFilter enabledFilter) {
    if (enabledFilter != null && !EnabledFilter.ALL.equals(enabledFilter)) {
      switch (enabledFilter) {
        case ENABLED_ONLY:
          must(matchIfGiven(GenericReferenceData.ENABLED, Boolean.TRUE));
          break;
        case DISABLED_ONLY:
          must(matchIfGiven(GenericReferenceData.ENABLED, Boolean.FALSE));
          break;
        default:
          throw new IllegalSwitchValueException(enabledFilter);
      }
    }
    return thisAsQ();
  }

  @Override
  public Q sort(Map<S, SortOrder> sortMap) {
    super.sort(sortMap);
    return thisAsQ();
  }
}
